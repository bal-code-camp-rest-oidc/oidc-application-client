package com.example.library.borrow.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BorrowBookRepository extends JpaRepository<BorrowBookEntity, UUID> {
  Optional<BorrowBookEntity> findOneByIdentifier(UUID identifier);

  void deleteBookByIdentifier(UUID identifier);
}
