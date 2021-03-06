package com.example.library.inventory.api;

import com.example.library.inventory.api.resource.BookResource;
import com.example.library.inventory.api.resource.assembler.BookResourceAssembler;
import com.example.library.inventory.business.BookService;
import com.example.library.inventory.dataaccess.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * REST api for books.
 */
@RestController
@RequestMapping("/books")
@Validated
public class BookRestController {

  private final BookService bookService;
  private final BookResourceAssembler bookResourceAssembler;

  @Autowired
  public BookRestController(BookService bookService, BookResourceAssembler bookResourceAssembler) {
    this.bookService = bookService;
    this.bookResourceAssembler = bookResourceAssembler;
  }

  @GetMapping
  public ResponseEntity<CollectionModel<BookResource>> getAllBooks() {
    return ResponseEntity.ok(bookResourceAssembler.toCollectionModel(bookService.findAll()));
  }

  @GetMapping("/{bookId}")
  public ResponseEntity<BookResource> getBookById(@PathVariable("bookId") UUID bookIdentifier) {
    return bookService.findByIdentifier(bookIdentifier)
               .map(bookResourceAssembler::toModel)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<BookResource> createBook(@RequestBody BookResource bookResource) {

    Book book = new Book(bookResource.getIdentifier(),
        bookResource.getIsbn(),
        bookResource.getTitle(),
        bookResource.getDescription(),
        bookResource.getAuthors());

    UUID identifier = bookService.create(book);

    return bookService.findByIdentifier(identifier)
               .map(bookResourceAssembler::toModel)
               .map(b -> {
                 URI location =
                     ServletUriComponentsBuilder.fromCurrentContextPath()
                         .path("/books/{bookId}")
                         .buildAndExpand(b.getIdentifier())
                         .toUri();
                 return ResponseEntity.created(location).body(b);
               })
               .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @PutMapping("/{bookId}")
  public ResponseEntity<BookResource> updateBook(
      @PathVariable("bookId") UUID bookId, @RequestBody BookResource bookResource) {

    return bookService.findByIdentifier(bookId)
               .map(b -> {
                 b.setAuthors(bookResource.getAuthors());
                 b.setDescription(bookResource.getDescription());
                 b.setIsbn(bookResource.getIsbn());
                 b.setTitle(bookResource.getTitle());
                 UUID identifier = bookService.update(b);
                 return bookService
                            .findWithDetailsByIdentifier(identifier)
                            .map(ub -> ResponseEntity.ok(bookResourceAssembler.toModel(ub)))
                            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
               })
               .orElse(ResponseEntity.notFound().build());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{bookId}")
  public void deleteBook(@PathVariable("bookId") UUID bookId) {
    bookService.deleteByIdentifier(bookId);
  }
}
