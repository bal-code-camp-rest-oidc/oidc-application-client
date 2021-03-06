package com.example.library.borrow.business;

import com.example.library.borrow.dataaccess.BorrowBookEntity;
import com.example.library.borrow.dataaccess.BorrowBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;
import java.util.UUID;

@Service
public class BorrowService {

    private final BorrowBookRepository borrowBookRepository;

    @Autowired
    public BorrowService(BorrowBookRepository borrowBookRepository) {
        this.borrowBookRepository = borrowBookRepository;
    }

    @Transactional
    @RolesAllowed("LIBRARY_USER")
    public void borrowById(UUID bookIdentifier, String userId) {
        if (bookIdentifier == null || userId == null) {
            throw new IllegalArgumentException("missing book or user ID");
        }

        borrowBookRepository.save(new BorrowBookEntity(bookIdentifier, userId));
    }

    @Transactional
    @RolesAllowed({"LIBRARY_USER", "LIBRARY_ADMIN"})
    public void returnById(UUID borrowBookId) {

        if (borrowBookId == null) {
            throw new IllegalArgumentException("missing borrow book ID");
        }

        borrowBookRepository.deleteBookByIdentifier(borrowBookId);
    }


    @Transactional
    public Optional<String> getBorrowerOfBook(UUID borrowBookId) {
        String user = borrowBookRepository.findOneByIdentifier(borrowBookId)
                .map(BorrowBookEntity::getUserId)
                .orElse(null);
        return user == null ? Optional.of("") : Optional.of(user);
    }
}
