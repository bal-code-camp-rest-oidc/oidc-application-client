package com.example.library.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Book {

    private UUID identifier;

    private String isbn;

    private String title;

    private String description;

    private List<String> authors = new ArrayList<>();

    public Book() {
    }

    public Book(
            UUID identifier,
            String isbn,
            String title,
            String description,
            List<String> authors) {
        this.identifier = identifier;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authors = authors;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(identifier, book.identifier) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(title, book.title) &&
                description.equals(book.description) &&
                Objects.equals(authors, book.authors);
    }

    @Override
    public String toString() {
        return "Book{" +
                "identifier=" + identifier +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", authors=" + authors +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, isbn, title, description, authors);
    }

}
