package com.example.library.inventory.api.resource.assembler;

import com.example.library.inventory.api.BookRestController;
import com.example.library.inventory.api.resource.BookResource;
import com.example.library.inventory.dataaccess.Book;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookResourceAssembler extends RepresentationModelAssemblerSupport<Book, BookResource> {

  public BookResourceAssembler() {
    super(BookRestController.class, BookResource.class);
  }

  @Override
  public BookResource toModel(Book book) {
    BookResource bookResource = new BookResource(book);
    bookResource.add(linkTo(methodOn(BookRestController.class).getBookById(book.getIdentifier())).withSelfRel());
    bookResource.add(linkTo(methodOn(BookRestController.class).updateBook(book.getIdentifier(), new BookResource()))
                         .withRel("update"));
    return bookResource;
  }

  @Override
  public CollectionModel<BookResource> toCollectionModel(Iterable<? extends Book> entities) {
    CollectionModel<BookResource> bookResources = super.toCollectionModel(entities);
    bookResources.add(
        linkTo(methodOn(BookRestController.class).getAllBooks()).withSelfRel(),
        linkTo(methodOn(BookRestController.class).createBook(new BookResource())).withRel("create"));
    return bookResources;
  }

}
