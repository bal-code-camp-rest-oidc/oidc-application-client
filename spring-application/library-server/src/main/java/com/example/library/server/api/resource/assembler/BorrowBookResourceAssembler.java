package com.example.library.server.api.resource.assembler;

import com.example.library.api.Book;
import com.example.library.api.User;
import com.example.library.server.api.BookRestController;
import com.example.library.server.api.resource.BorrowBookResource;
import com.example.library.server.business.BookService;
import com.example.library.server.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BorrowBookResourceAssembler extends RepresentationModelAssemblerSupport<Book, BorrowBookResource> {

    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BorrowBookResourceAssembler(BookService bookService, UserService userService) {
        super(BookRestController.class, BorrowBookResource.class);

        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public BorrowBookResource toModel(Book book) {
        String userid = bookService.getBorrowedByOfBook(book);
        User user = userid == null || userid.equals("") ? null : userService.findByIdentifier(userid).orElse(null);
        BorrowBookResource borrowBookResource = new BorrowBookResource(book, user);
        borrowBookResource.add(
                linkTo(methodOn(BookRestController.class).getBookById(book.getIdentifier())).withSelfRel());
        borrowBookResource.add(
                linkTo(
                        methodOn(BookRestController.class)
                                .updateBook(book.getIdentifier(), new BorrowBookResource()))
                        .withRel("update"));
        borrowBookResource.add(
                linkTo(
                        methodOn(BookRestController.class)
                                .borrowBookById(book.getIdentifier(), null))
                        .withRel("borrow"));
        borrowBookResource.add(
                linkTo(
                        methodOn(BookRestController.class)
                                .returnBookById(book.getIdentifier(), null))
                        .withRel("return"));
        return borrowBookResource;
    }

    @Override
    public CollectionModel<BorrowBookResource> toCollectionModel(Iterable<? extends Book> entities) {
        CollectionModel<BorrowBookResource> bookResources = super.toCollectionModel(entities);
        bookResources.add(
                linkTo(methodOn(BookRestController.class).getAllBooks()).withSelfRel(),
                linkTo(methodOn(BookRestController.class).createBook(new BorrowBookResource()))
                        .withRel("create"));
        return bookResources;
    }

}
