package com.example.library.server.api;

import com.example.library.api.Book;
import com.example.library.api.User;
import com.example.library.server.api.resource.BorrowBookResource;
import com.example.library.server.api.resource.assembler.BorrowBookResourceAssembler;
import com.example.library.server.business.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final BorrowBookResourceAssembler borrowBookResourceAssembler;

    @Autowired
    public BookRestController(BookService bookService, BorrowBookResourceAssembler borrowBookResourceAssembler) {
        this.bookService = bookService;
        this.borrowBookResourceAssembler = borrowBookResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<BorrowBookResource>> getAllBooks() {
        return ResponseEntity.ok(borrowBookResourceAssembler.toCollectionModel(bookService.findAll()));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BorrowBookResource> getBookById(@PathVariable("bookId") UUID bookIdentifier) {
        return bookService
                .findWithDetailsByIdentifier(bookIdentifier)
                .map(borrowBookResourceAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<BorrowBookResource> borrowBookById(
            @PathVariable("bookId") UUID bookId, @AuthenticationPrincipal User libraryUser) {
        return bookService
                .findByIdentifier(bookId)
                .map(
                        b -> {
                            bookService.borrowById(bookId, libraryUser.getUserName());
                            return bookService
                                    .findWithDetailsByIdentifier(b.getIdentifier())
                                    .map(bb -> ResponseEntity.ok(borrowBookResourceAssembler.toModel(bb)))
                                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<BorrowBookResource> returnBookById(
            @PathVariable("bookId") UUID bookId, @AuthenticationPrincipal User libraryUser) {
        return bookService
                .findByIdentifier(bookId)
                .map(
                        b -> {
                            bookService.returnById(bookId, libraryUser.getUserName());
                            return bookService
                                    .findWithDetailsByIdentifier(b.getIdentifier())
                                    .map(bb -> ResponseEntity.ok(borrowBookResourceAssembler.toModel(bb)))
                                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BorrowBookResource> createBook(@RequestBody BorrowBookResource borrowBookResource) {

        Book book =
                new Book(
                        borrowBookResource.getIdentifier(),
                        borrowBookResource.getIsbn(),
                        borrowBookResource.getTitle(),
                        borrowBookResource.getDescription(),
                        borrowBookResource.getAuthors());

        UUID identifier = bookService.create(book);

        return bookService
                .findWithDetailsByIdentifier(identifier)
                .map(borrowBookResourceAssembler::toModel)
                .map(
                        b -> {
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
    public ResponseEntity<BorrowBookResource> updateBook(
            @PathVariable("bookId") UUID bookId, @RequestBody BorrowBookResource borrowBookResource) {

        return bookService
                .findByIdentifier(bookId)
                .map(
                        b -> {
                            b.setAuthors(borrowBookResource.getAuthors());
                            b.setDescription(borrowBookResource.getDescription());
                            b.setIsbn(borrowBookResource.getIsbn());
                            b.setTitle(borrowBookResource.getTitle());
                            UUID identifier = bookService.update(b);
                            return bookService
                                    .findWithDetailsByIdentifier(identifier)
                                    .map(ub -> ResponseEntity.ok(borrowBookResourceAssembler.toModel(ub)))
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
