package com.example.library.borrow.business;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class BorrowService {

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_USER')")
  public void borrowById(UUID bookIdentifier, UUID userIdentifier) {

    if (bookIdentifier == null || userIdentifier == null) {
      return;
    }

    userRepository
        .findOneByIdentifier(userIdentifier)
        .ifPresent(
            u ->
                bookRepository
                    .findOneByIdentifier(bookIdentifier)
                    .ifPresent(
                        b -> {
                          doBorrow(b, u);
                          bookRepository.save(b);
                        }));
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_USER')")
  public void returnById(UUID bookIdentifier, UUID userIdentifier) {

    if (bookIdentifier == null || userIdentifier == null) {
      return;
    }

    userRepository
        .findOneByIdentifier(userIdentifier)
        .ifPresent(
            u ->
                bookRepository
                    .findOneByIdentifier(bookIdentifier)
                    .ifPresent(
                        b -> {
                          doReturn(b, u);
                          bookRepository.save(b);
                        }));
  }

  private void doReturn(Book book, User user) {
    if (book.isBorrowed()) {
      if (book.getBorrowedBy().equals(user)) {
        book.setBorrowed(false);
        book.setBorrowedBy(null);
      } else {
        throw new AccessDeniedException(
            String.format(
                "User %s cannot return a book borrowed by another user", user.getEmail()));
      }
    }
  }

  private void doBorrow(Book book, User user) {
    if (!book.isBorrowed()) {
      book.setBorrowed(true);
      book.setBorrowedBy(user);
    }
  }
}
