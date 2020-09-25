package com.example.library.borrow.dataaccess;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class BorrowBookEntity extends AbstractPersistable<Long> {

    @NotNull
    private UUID identifier;

    @NotNull
    @Size(min = 1, max = 255)
    private String userId;

    @NotNull
    private UUID bookId;

    @SuppressWarnings("unused")
    public BorrowBookEntity() {
    }

    @PersistenceConstructor
    public BorrowBookEntity(
            UUID identifier,
            UUID bookId,
            String userId) {
        this.identifier = identifier;
        this.bookId = bookId;
        this.userId = userId;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BorrowBookEntity{" +
                "identifier=" + identifier +
                ", userId='" + userId + '\'' +
                ", bookId=" + bookId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BorrowBookEntity that = (BorrowBookEntity) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, userId, bookId);
    }
}
