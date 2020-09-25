package com.example.library.borrow.api;

import com.example.library.borrow.business.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/borrowBooks")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping()
    public ResponseEntity<UUID> borrowBookById(
            @PathVariable("bookId") String bookId, @AuthenticationPrincipal OidcUser principal) {
        //todo: REST call, validate if book exists
        return ResponseEntity.of(Optional.of(borrowService.borrowById(UUID.fromString(bookId), principal.getName())));
    }

    @DeleteMapping("/{borrowBookId}")
    public void returnBookById(
            @PathVariable("borrowBookId") String borrowBookId) {
        borrowService.returnById(UUID.fromString(borrowBookId));
    }
}
