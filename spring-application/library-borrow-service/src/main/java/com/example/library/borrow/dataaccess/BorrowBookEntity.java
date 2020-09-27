package com.example.library.borrow.dataaccess;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

@Entity
public class BorrowBookEntity extends AbstractPersistable<Long> {
  @NotNull
  @Size(min = 1, max = 255)
  private String userId;

  @NotNull
  private UUID identifier;

  @SuppressWarnings("unused")
  public BorrowBookEntity() {
  }

  @PersistenceConstructor
  public BorrowBookEntity(
      UUID bookId,
      String userId) {
    this.identifier = bookId;
    this.userId = userId;
  }

  public UUID getBookId() {
    return identifier;
  }

  public void setBookId(UUID bookId) {
    this.identifier = bookId;
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
               ", userId='" + userId + '\'' +
               ", bookId=" + identifier +
               '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    BorrowBookEntity that = (BorrowBookEntity) o;
    return Objects.equals(userId, that.userId) &&
               Objects.equals(identifier, that.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), userId, identifier);
  }
}
