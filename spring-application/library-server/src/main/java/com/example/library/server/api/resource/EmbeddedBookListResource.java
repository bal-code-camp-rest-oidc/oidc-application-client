package com.example.library.server.api.resource;

import java.util.Collection;

public class EmbeddedBookListResource {

    private Collection<BookResource> bookResourceList;

    public EmbeddedBookListResource() {
    }

    public EmbeddedBookListResource(Collection<BookResource> books) {
        this.bookResourceList = books;
    }

    public Collection<BookResource> getBookResourceList() {
        return bookResourceList;
    }

    public void setBookResourceList(Collection<BookResource> books) {
        this.bookResourceList = books;
    }
}