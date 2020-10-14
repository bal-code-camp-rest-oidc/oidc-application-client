package com.example.library.server.api.resource.assembler;

import com.example.library.api.Book;
import com.example.library.server.api.BookRestController;
import com.example.library.server.api.resource.BookListResource;
import com.example.library.server.api.resource.BookResource;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookResourceAssembler extends RepresentationModelAssemblerSupport<Book, BookResource> {

    public BookResourceAssembler() {
        super(BookRestController.class, BookResource.class);
    }

    @Override
    public BookResource toModel(Book book) {
        return new BookResource(book);
    }

    public Book fromModel(BookResource bookResource) {
        Book book = new Book();
        book.setAuthors(bookResource.getAuthors());
        book.setDescription(bookResource.getDescription());
        book.setIdentifier(bookResource.getIdentifier());
        book.setIsbn(bookResource.getIsbn());
        book.setTitle(bookResource.getTitle());
        return book;
    }

    public List<Book> fromCollectionModel(BookListResource bookResources) {
        ArrayList<Book> bookList = new ArrayList<>();
        for (BookResource book : bookResources.get_embedded().getBookResourceList()) {
            bookList.add(fromModel(book));
        }
        return bookList;
    }
}
