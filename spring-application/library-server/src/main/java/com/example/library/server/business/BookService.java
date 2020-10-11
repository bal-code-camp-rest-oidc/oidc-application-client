package com.example.library.server.business;

import com.example.library.api.Book;
import com.example.library.api.User;
import com.example.library.server.api.resource.BookResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookService {

    @Value("${library.inventory-srv}")
    private String bookServiceUri;

    @Value("${library.borrow-srv}")
    private String borrowServiceUri;

    private final UserService userService;
    private final WebClient webClient;

    @Autowired
    public BookService(UserService userService, WebClient webClient) {
        this.userService = userService;
        this.webClient = webClient;
    }

    @Transactional
    @PreAuthorize("hasRole('LIBRARY_CURATOR')")
    public UUID create(Book book) {
        return webClient
                .post()
                .uri(bookServiceUri + "/books")
                .body(Mono.just(book), Book.class)
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        cr -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
                .bodyToMono(BookResource.class)
                .log()
                .blockOptional().map(r -> r.getIdentifier()).orElse(null)
                ;
    }

    @Transactional
    @PreAuthorize("hasRole('LIBRARY_CURATOR')")
    public UUID update(Book book) {
        return webClient
                .put()
                .uri(bookServiceUri + "/books")
                .body(Mono.just(book), Book.class)
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        cr -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
                .bodyToMono(BookResource.class)
                .log()
                .blockOptional().map(r -> r.getIdentifier()).orElse(null)
                ;
    }

    @PreAuthorize("isAuthenticated()")
    public Optional<Book> findByIdentifier(UUID uuid) {
        return findWithDetailsByIdentifier(uuid);
    }

    @PreAuthorize("isAuthenticated()")
    public Optional<Book> findWithDetailsByIdentifier(UUID uuid) {
        return webClient
                .get()
                .uri(bookServiceUri + "/books/" + uuid.toString())
                .retrieve()
                .bodyToMono(Book.class)
                .log()
                .blockOptional()
                ;
    }

    @Transactional
    @PreAuthorize("hasRole('LIBRARY_USER')")
    public void borrowById(UUID bookIdentifier, String userName) {
        if (bookIdentifier == null || userName == null) {
            return;
        }

        userService.findByIdentifier(userName).ifPresent(
                u ->
                        this.findByIdentifier(bookIdentifier)
                                .ifPresent(
                                        b -> {
                                            doBorrow(b, u);
                                            this.update(b);
                                        }));
    }

    @Transactional
    @PreAuthorize("hasRole('LIBRARY_USER')")
    public void returnById(UUID bookIdentifier, String userName) {

        if (bookIdentifier == null || userName == null) {
            return;
        }

        userService
                .findByIdentifier(userName)
                .ifPresent(
                        u ->
                                findByIdentifier(bookIdentifier)
                                        .ifPresent(
                                                b -> {
                                                    doReturn(b, u);
                                                    update(b);
                                                }));
    }

    @PreAuthorize("isAuthenticated()")
    public List<Book> findAll() {
        return webClient
                .get()
                .uri(bookServiceUri + "/books")
                .retrieve()
                .bodyToMono(Book[].class)
                .log()
                .map(Arrays::asList)
                .block()
                ;
    }

    @Transactional
    @PreAuthorize("hasRole('LIBRARY_CURATOR')")
    public void deleteByIdentifier(UUID bookIdentifier) {
        webClient
                .delete()
                .uri(bookServiceUri + "/books/" + bookIdentifier.toString())
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        cr -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        ;
    }

    private void doReturn(Book book, User user) {
        String borrowedBy = getBorrowedByOfBook(book);

        if (borrowedBy != null && !borrowedBy.equals("")) {
            if (user.getUserName().equals(borrowedBy)) {
                webClient
                        .delete()
                        .uri(borrowServiceUri + "/borrowBooks/" + book.getIdentifier())
                        .retrieve()
                        .onStatus(
                                s -> s.equals(HttpStatus.UNAUTHORIZED),
                                cr -> Mono.just(new BadCredentialsException("Not authenticated")))
                        .onStatus(
                                HttpStatus::is4xxClientError,
                                cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
                        .onStatus(
                                HttpStatus::is5xxServerError,
                                cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
                        .bodyToMono(String.class)
                        .block()
                ;
            } else {
                throw new AccessDeniedException(
                        String.format(
                                "User %s cannot return a book borrowed by another user", user.getEmail()));
            }
        }
    }

    private void doBorrow(Book book, User user) {
        String borrowedBy = getBorrowedByOfBook(book);

        if (borrowedBy == null || borrowedBy.equals("")) {
            webClient
                    .post()
                    .uri(borrowServiceUri + "/borrowBooks/" + book.getIdentifier())
                    .body(Mono.just(user), User.class)
                    .retrieve()
                    .onStatus(
                            s -> s.equals(HttpStatus.UNAUTHORIZED),
                            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
                    .onStatus(
                            HttpStatus::is4xxClientError,
                            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
                    .onStatus(
                            HttpStatus::is5xxServerError,
                            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
                    .bodyToMono(String.class)
                    .block()
            ;
        }
    }

    public String getBorrowedByOfBook(Book book) {
        return webClient
                .get()
                .uri(borrowServiceUri + "/borrowBooks/" + book.getIdentifier())
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        cr -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
                .bodyToMono(String.class)
                .block()
                ;
    }
}
