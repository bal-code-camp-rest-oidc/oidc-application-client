package com.example.library.inventory.business;

import com.example.library.inventory.dataaccess.Book;
import com.example.library.inventory.dataaccess.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookService {

  private final BookRepository bookRepository;
  private final IdGenerator idGenerator;

  @Autowired
  public BookService(
      BookRepository bookRepository, IdGenerator idGenerator) {
    this.bookRepository = bookRepository;
    this.idGenerator = idGenerator;
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public UUID create(Book book) {
    if (book.getIdentifier() == null) {
      book.setIdentifier(idGenerator.generateId());
    }
    return bookRepository.save(book).getIdentifier();
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public UUID update(Book book) {
    return bookRepository.save(book).getIdentifier();
  }

  @PreAuthorize("isAuthenticated()")
  public Optional<Book> findByIdentifier(UUID uuid) {
    return bookRepository.findOneByIdentifier(uuid);
  }

  @PreAuthorize("isAuthenticated()")
  public Optional<Book> findWithDetailsByIdentifier(UUID uuid) {
    return bookRepository.findOneWithDetailsByIdentifier(uuid);
  }

  @PreAuthorize("isAuthenticated()")
  public List<Book> findAll() {
    return bookRepository.findAll();
  }

  @Transactional
  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public void deleteByIdentifier(UUID bookIdentifier) {
    bookRepository.deleteBookByIdentifier(bookIdentifier);
  }
}
