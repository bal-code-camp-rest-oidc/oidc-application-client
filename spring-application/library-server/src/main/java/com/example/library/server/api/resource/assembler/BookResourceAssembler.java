package com.example.library.server.api.resource.assembler;

import ch.baloise.keycloak.client.admin.api.Book;
import ch.baloise.keycloak.client.admin.api.User;
import com.example.library.server.api.BookRestController;
import com.example.library.server.api.resource.BookResource;
import com.example.library.server.business.BookService;
import com.example.library.server.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookResourceAssembler extends RepresentationModelAssemblerSupport<Book, BookResource> {

  private final BookService bookService;
  private final UserService userService;

  @Autowired
  public BookResourceAssembler(BookService bookService, UserService userService) {
    super(BookRestController.class, BookResource.class);

    this.bookService = bookService;
    this.userService = userService;
  }

  @Override
  public BookResource toModel(Book book) {
    String userid = bookService.getBorrowedByOfBook(book);
    User user = userid == null ? null : userService.findByIdentifier(userid).orElse(null);
    BookResource bookResource = new BookResource(book, user);
    bookResource.add(
        linkTo(methodOn(BookRestController.class).getBookById(book.getIdentifier())).withSelfRel());
    bookResource.add(
        linkTo(
            methodOn(BookRestController.class)
                .updateBook(book.getIdentifier(), new BookResource()))
            .withRel("update"));
    bookResource.add(
        linkTo(
            methodOn(BookRestController.class)
                .borrowBookById(book.getIdentifier(), new User()))
            .withRel("borrow"));
    bookResource.add(
        linkTo(
            methodOn(BookRestController.class)
                .returnBookById(book.getIdentifier(), new User()))
            .withRel("return"));
    return bookResource;
  }

  @Override
  public CollectionModel<BookResource> toCollectionModel(Iterable<? extends Book> entities) {
    CollectionModel<BookResource> bookResources = super.toCollectionModel(entities);
    bookResources.add(
        linkTo(methodOn(BookRestController.class).getAllBooks()).withSelfRel(),
        linkTo(methodOn(BookRestController.class).createBook(new BookResource()))
            .withRel("create"));
    return bookResources;
  }

}
