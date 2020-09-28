package com.example.library.client.web;

import com.example.library.api.User;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BookResource {

    private UUID identifier;

    private String isbn;

    private String title;

    private String description;

    private boolean borrowed;

    private List<String> authors = new ArrayList<>();

    private User borrowedBy;

    public BookResource() {
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

    /**
     * Only allow returning book if user handed over by OIDC and borrowed by eMail
     * are equal.
     *
     * @param user the OIDC user that will be checked using the eMail address.
     * @return true, if borrower and user are equal.
     */
    public boolean returnBookAllowed(OidcUser user) {
        if (!isBorrowed()) {
            return false;
        }

        if (user != null) {
            return borrowedBy != null && borrowedBy.getEmail().equals(user.getEmail());
        } else {
            // Always fail secure
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookResource bookResource = (BookResource) o;
        return borrowed == bookResource.borrowed
                && identifier.equals(bookResource.identifier)
                && isbn.equals(bookResource.isbn)
                && title.equals(bookResource.title)
                && description.equals(bookResource.description)
                && authors.equals(bookResource.authors)
                && Objects.equals(borrowedBy, bookResource.borrowedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(), identifier, isbn, title, description, borrowed, authors, borrowedBy);
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
                + ", borrowed="
                + borrowed
                + ", authors="
                + authors
                + ", borrowedBy="
                + borrowedBy
                + '}';
    }
}
