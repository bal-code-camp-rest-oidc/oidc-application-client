package com.example.library.borrow.business;

import com.example.library.borrow.dataaccess.BorrowBookEntity;
import com.example.library.borrow.dataaccess.BorrowBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BorrowService {

  private final BorrowBookRepository borrowBookRepository;

  @Autowired
  public BorrowService(BorrowBookRepository borrowBookRepository) {
    this.borrowBookRepository = borrowBookRepository;
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_USER')")
  public UUID borrowById(UUID bookIdentifier, String userId) {

    if (bookIdentifier == null || userId == null) {
      throw new IllegalArgumentException("missing book or user ID");
    }

    UUID borrowBookId = UUID.randomUUID();
    borrowBookRepository.save(new BorrowBookEntity(borrowBookId, bookIdentifier, userId));

    return borrowBookId;
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_USER')")
  public void returnById(UUID borrowBookId) {

    if (borrowBookId == null) {
      throw new IllegalArgumentException("missing borrow book ID");
    }

    borrowBookRepository.deleteBookByIdentifier(borrowBookId);
  }
}
