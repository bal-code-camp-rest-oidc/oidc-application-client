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

    private BookResource borrowedBook;

    public BorrowBookResource() {
    }

    public BorrowBookResource(BorrowBook borrowBook) {
        this(
                borrowBook.getBook() != null
                        ? new BorrowedBookResourceAssembler().toModel(borrowBook.getBook())
                        : null),
                borrowBook.getBorrowedBy() != null
                        ? new UserResourceAssembler().toModel(borrowBook.getBorrowedBy())
                        : null);
    }

    public BorrowBookResource(
            UUID identifier,
            String isbn,
            String title,
            String description,
            List<String> authors,
            boolean borrowed,
            UserResource borrowedBy) {
        this.identifier = identifier;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.borrowed = borrowed;
        this.borrowedBy = borrowedBy;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public UserResource getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(UserResource borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BorrowBookResource that = (BorrowBookResource) o;
        return borrowed == that.borrowed
                && identifier.equals(that.identifier)
                && isbn.equals(that.isbn)
                && title.equals(that.title)
                && description.equals(that.description)
                && authors.equals(that.authors)
                && Objects.equals(borrowedBy, that.borrowedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(), identifier, isbn, title, description, authors, borrowed, borrowedBy);
    }

    @Override
    public String toString() {
        return "BookResource{"
                + "identifier="
                + identifier
                + ", isbn='"
                + isbn
                + '\''
                + ", title='"
                + title
                + '\''
                + ", description='"
                + description
                + '\''
                + ", authors="
                + authors
                + ", borrowed="
                + borrowed
                + ", borrowedBy="
                + borrowedBy
                + '}';
    }
}
