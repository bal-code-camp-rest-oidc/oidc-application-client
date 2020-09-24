package ch.baloise.keycloak.client.admin.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Book {

    private UUID identifier;

    private String isbn;

    private String title;

    private String description;

    private boolean borrowed;

    private List<String> authors = new ArrayList<>();

    private User borrowedBy;

    public Book() {
    }

    public Book(
            UUID identifier,
            String isbn,
            String title,
            String description,
            List<String> authors,
            boolean borrowed,
            User borrowedBy) {
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

    public User getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(User borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public void doBorrow(User user) {
        if (!this.borrowed) {
            this.borrowed = true;
            this.borrowedBy = user;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return borrowed == book.borrowed
                && identifier.equals(book.identifier)
                && isbn.equals(book.isbn)
                && title.equals(book.title)
                && description.equals(book.description)
                && authors.equals(book.authors)
                && Objects.equals(borrowedBy, book.borrowedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(), identifier, isbn, title, description, borrowed, authors, borrowedBy);
    }

    @Override
    public String toString() {
        return "Book{"
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
                + ", borrowed="
                + borrowed
                + ", authors="
                + authors
                + ", borrowedBy="
                + borrowedBy
                + '}';
    }
}
