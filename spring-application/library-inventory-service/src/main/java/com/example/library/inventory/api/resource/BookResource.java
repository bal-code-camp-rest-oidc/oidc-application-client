package com.example.library.inventory.api.resource;

import com.example.library.inventory.dataaccess.Book;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BookResource extends RepresentationModel<BookResource> {

  private UUID identifier;

  @NotNull
  @Size(min = 1, max = 50)
  private String isbn;

  @NotNull
  @Size(min = 1, max = 50)
  private String title;

  @NotNull
  @Size(min = 1, max = 50)
  private String description;

  @NotNull
  private List<String> authors;

  public BookResource() {
  }

  public BookResource(Book book) {
    this(book.getIdentifier(),
        book.getIsbn(),
        book.getTitle(),
        book.getDescription(),
        book.getAuthors());
  }

  public BookResource(UUID identifier,
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
    if (!super.equals(o)) return false;
    BookResource that = (BookResource) o;
    return identifier.equals(that.identifier)
               && isbn.equals(that.isbn)
               && title.equals(that.title)
               && description.equals(that.description)
               && authors.equals(that.authors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), identifier, isbn, title, description, authors);
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
               + '}';
  }
}
