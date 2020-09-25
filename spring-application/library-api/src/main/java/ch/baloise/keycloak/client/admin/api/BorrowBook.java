package ch.baloise.keycloak.client.admin.api;

import java.util.Objects;

public class BorrowBook {

    private Book book;

    private User borrowedBy;

    public BorrowBook(Book book, User borrowedBy) {
        this.book = book;
        this.borrowedBy = borrowedBy;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(User borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    @Override
    public String toString() {
        return "BorrowBook{" +
                "borrowedBook=" + book +
                ", borrowedBy=" + borrowedBy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowBook that = (BorrowBook) o;
        return book.equals(that.book) &&
                borrowedBy.equals(that.borrowedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, borrowedBy);
    }
}
