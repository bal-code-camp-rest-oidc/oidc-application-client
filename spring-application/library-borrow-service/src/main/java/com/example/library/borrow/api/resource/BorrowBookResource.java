package com.example.library.borrow.api.resource;

import ch.baloise.keycloak.client.admin.api.BorrowBook;
import com.example.library.inventory.dataaccess.Book;
import com.example.library.server.api.resource.assembler.UserResourceAssembler;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BorrowBookResource extends RepresentationModel<BorrowBookResource> {

    private UserResource borrowedBy;

    private BookResource borrowBook;

    public BorrowBookResource() {
    }

    public BorrowBookResource(BorrowBook borrowBook) {
        this(
                borrowBook.getBook() != null
                        ? new BorrowBookResourceAssembler().toModel(borrowBook.getBook())
                        : null),
                borrowBook.getBorrowedBy() != null
                        ? new UserResourceAssembler().toModel(borrowBook.getBorrowedBy())
                        : null);
    }

    public BorrowBookResource(
            BorrowBookResource borrowBook, UserResource borrowedBy) {
        this.borrowBook = borrowBook;
        this.borrowedBy = borrowedBy;
    }

    public UserResource getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(UserResource borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public BorrowBookResource getBorrowBook() {
        return borrowBook;
    }

    public void setBorrowBook(BorrowBookResource borrowBook) {
        this.borrowBook = borrowBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BorrowBookResource that = (BorrowBookResource) o;
        return borrowedBy.equals(that.borrowedBy) &&
                borrowBook.equals(that.borrowBook);
    }

    @Override
    public String toString() {
        return "BorrowBookResource{" +
                "borrowedBy=" + borrowedBy +
                ", borrowBook=" + borrowBook +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), borrowedBy, borrowBook);
    }
}
