package com.example.library.borrow.api;

import com.example.library.borrow.business.BorrowService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/borrowBooks")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @ApiOperation(value = "Marks a book to be borrowed by the specified principal. This method should be called, whenever you borrow for yourself.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "the list of all registered users"),
            @ApiResponse(code = 401, message = "You are not authorized to view the list"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping("/{borrowBookId}")
    public void borrowBookById(
            @PathVariable("borrowBookId") String bookId, Principal principal) {
        //todo: REST call, validate if book exists
        borrowService.borrowById(UUID.fromString(bookId), principal.getName());
    }

    @DeleteMapping("/{borrowBookId}")
    public void returnBookById(@PathVariable("borrowBookId") String borrowBookId) {
        borrowService.returnById(UUID.fromString(borrowBookId));
    }

    @GetMapping("/{borrowBookId}")
    public ResponseEntity<String> getBorrowerForBook(@PathVariable("borrowBookId") String bookId) {
        return ResponseEntity.of(borrowService.getBorrowerOfBook(UUID.fromString(bookId)));
    }
}
