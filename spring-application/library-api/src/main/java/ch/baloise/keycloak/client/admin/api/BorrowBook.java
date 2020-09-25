package ch.baloise.keycloak.client.admin.api;

import java.util.Objects;
import java.util.UUID;

public class BorrowBook {

    private UUID borrowId;

    private UUID bookId;

    private String borrowerId;

    public BorrowBook(UUID bookId, String borrowerId) {
        this.bookId = bookId;
        this.borrowerId = borrowerId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowBook that = (BorrowBook) o;
        return borrowId.equals(that.borrowId) &&
                bookId.equals(that.bookId) &&
                borrowerId.equals(that.borrowerId);
    }

    @Override
    public String toString() {
        return "BorrowBook{" +
                "borrowId=" + borrowId +
                ", bookId=" + bookId +
                ", borrowerId='" + borrowerId + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(borrowId, bookId, borrowerId);
    }

}
