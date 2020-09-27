package com.example.library.server.business;

import ch.baloise.keycloak.client.admin.api.Book;
import ch.baloise.keycloak.client.admin.api.User;
import com.example.library.server.api.resource.BookResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookService {

  @Value("library.inventory-srv")
  private String bookServiceUri;

  UserService userService;

  @Value("library.borrow-srv")
  private String borrowServiceUri;

  @Autowired
  public BookService(UserService userService) {
    this.userService = userService;
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public UUID create(Book book) {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(bookServiceUri)
                                    .build();
    ResponseEntity<BookResource> response = restTemplate.postForEntity("", book, BookResource.class);
    return response.getBody() == null ? null : response.getBody().getIdentifier();
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public UUID update(Book book) {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(bookServiceUri)
                                    .build();
    restTemplate.put(book.getIdentifier().toString(), book);
    return book.getIdentifier();
  }

  @PreAuthorize("isAuthenticated()")
  public Optional<Book> findByIdentifier(UUID uuid) {
    return findWithDetailsByIdentifier(uuid);
  }

  @PreAuthorize("isAuthenticated()")
  public Optional<Book> findWithDetailsByIdentifier(UUID uuid) {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(bookServiceUri)
                                    .build();
    ResponseEntity<Book> response = restTemplate.getForEntity(uuid.toString(), Book.class);
    return response.getBody() == null ? Optional.empty() : Optional.of(response.getBody());
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
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(bookServiceUri)
                                    .build();
    ResponseEntity<Book[]> response = restTemplate.getForEntity("", Book[].class);
    return response.getBody() == null ? null : Arrays.asList(response.getBody());
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public void deleteByIdentifier(UUID bookIdentifier) {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(bookServiceUri)
                                    .build();
    restTemplate.delete(bookIdentifier.toString());
  }

  private void doReturn(Book book, User user) {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(borrowServiceUri)
                                    .build();
    String borrowedBy = restTemplate.getForEntity("/borrowBooks/" + book.getIdentifier(), String.class).getBody();

    if (borrowedBy != null && !borrowedBy.equals("")) {
      if (user.getUserName().equals(borrowedBy)) {


        RestTemplate bookRestTemplate = new RestTemplateBuilder()
                                            // todo auth
                                            .rootUri(bookServiceUri)
                                            .build();
        bookRestTemplate.delete("/borrowBooks/" + book.getIdentifier());
      } else {
        throw new AccessDeniedException(
            String.format(
                "User %s cannot return a book borrowed by another user", user.getEmail()));
      }
    }
  }

  private void doBorrow(Book book, User user) {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(borrowServiceUri)
                                    .build();
    String borrowedBy = restTemplate.getForEntity("/borrowBooks/" + book.getIdentifier(), String.class).getBody();

    if (borrowedBy == null || borrowedBy.equals("")) {
      RestTemplate borrowRestTemplate = new RestTemplateBuilder()
                                            // todo auth
                                            .rootUri(borrowServiceUri)
                                            .build();
      borrowRestTemplate.postForEntity("/borrowBooks/" + book.getIdentifier(), user, String.class);
    }
  }

  public String getBorrowedByOfBook(Book book) {
    RestTemplate restTemplate = new RestTemplateBuilder()
            // todo auth
            .rootUri(borrowServiceUri)
            .build();
    String borrowedBy = restTemplate.getForEntity("/borrowBooks/" + book.getIdentifier(), String.class).getBody();
    return borrowedBy == null ? null : borrowedBy;
  }
}
