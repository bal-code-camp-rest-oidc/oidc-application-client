package com.example.library.server.business;

import com.example.library.api.Book;
import com.example.library.api.User;
import com.example.library.server.api.resource.BookListResource;
import com.example.library.server.api.resource.BookResource;
import com.example.library.server.api.resource.BorrowBookResource;
import com.example.library.server.api.resource.assembler.BookResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
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
    private final BookResourceAssembler bookResourceAssembler;

    @Autowired
    public BookService(BookResourceAssembler bookResourceAssembler, UserService userService, WebClient webClient) {
        this.bookResourceAssembler = bookResourceAssembler;
        this.userService = userService;
        this.webClient = webClient;
    }

    @Transactional
    @PreAuthorize("hasRole('LIBRARY_CURATOR')")
    public UUID create(Book book) {
        return webClient
                .post()
                .uri(bookServiceUri + "/books")
                .body(Mono.just(bookResourceAssembler.toModel(book)), BookResource.class)
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
                .bodyToMono(BorrowBookResource.class)
                .log()
                .blockOptional().map(BorrowBookResource::getIdentifier).orElse(null)
                ;
    }

    @Transactional
    @PreAuthorize("hasRole('LIBRARY_CURATOR')")
    public UUID update(Book book) {
        return webClient
                .put()
                .uri(bookServiceUri + "/books")
                .body(Mono.just(bookResourceAssembler.toModel(book)), BookResource.class)
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
                .bodyToMono(BorrowBookResource.class)
                .log()
                .blockOptional().map(BorrowBookResource::getIdentifier).orElse(null)
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
                .bodyToMono(BookResource.class)
                .map(bookResourceAssembler::fromModel)
                .log()
                .blockOptional()
                ;
    }

    @Transactional
    //@PreAuthorize("hasRole('LIBRARY_USER')")
    @RolesAllowed("LIBRARY_USER")
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
                                        }));
    }

    @Transactional
    //@PreAuthorize("hasRole('LIBRARY_USER')")
    @RolesAllowed({"LIBRARY_USER", "LIBRARY_ADMIN"})
    public void returnById(UUID bookIdentifier, Principal currentUser) {
        if (bookIdentifier == null || currentUser == null) {
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final boolean isAdmin = authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("SCOPE_library_admin"));
        userService
                .findByIdentifier(currentUser.getName())
                .ifPresent(
                        u ->
                                findByIdentifier(bookIdentifier)
                                        .ifPresent(
                                                b -> {
                                                    doReturn(
                                                            b, u, isAdmin);
                                                }));
    }

    @PreAuthorize("isAuthenticated()")
    public List<Book> findAll() {
        return webClient
                .get()
                .uri(bookServiceUri + "/books")
                .retrieve()
                .bodyToMono(BookListResource.class)
                .map(bookResourceAssembler::fromCollectionModel)
                .log()
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

    private void doReturn(Book book, User user, boolean isAdmin) {
        String borrowedBy = getBorrowedByOfBook(book);

        if (borrowedBy != null && !borrowedBy.equals("")) {
            if (isAdmin || user.getIdentifier().toString().equals(borrowedBy)) {
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
                .blockOptional().orElse(null)
                ;
    }
}
