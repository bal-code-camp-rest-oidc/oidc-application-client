package com.example.library.borrow.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

public class BorrowController {
  @PostMapping("/{bookId}/borrow")
  public ResponseEntity<BookResource> borrowBookById(
      @PathVariable("bookId") UUID bookId, @AuthenticationPrincipal LibraryUser libraryUser) {
    return bookService.findByIdentifier(bookId)
               .map(b -> {
                 bookService.borrowById(bookId, libraryUser.getIdentifier());
                 return bookService
                            .findWithDetailsByIdentifier(b.getIdentifier())
                            .map(bb -> ResponseEntity.ok(bookResourceAssembler.toModel(bb)))
                            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
               })
               .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{bookId}/return")
  public ResponseEntity<BookResource> returnBookById(
      @PathVariable("bookId") UUID bookId, @AuthenticationPrincipal LibraryUser libraryUser) {
    return bookService.findByIdentifier(bookId)
               .map(b -> {
                 bookService.returnById(bookId, libraryUser.getIdentifier());
                 return bookService
                            .findWithDetailsByIdentifier(b.getIdentifier())
                            .map(bb -> ResponseEntity.ok(bookResourceAssembler.toModel(bb)))
                            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
               })
               .orElse(ResponseEntity.notFound().build());
  }
}
