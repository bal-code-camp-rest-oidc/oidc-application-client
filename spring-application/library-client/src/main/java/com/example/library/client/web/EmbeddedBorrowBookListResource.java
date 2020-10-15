package com.example.library.client.web;

import java.util.Collection;

public class EmbeddedBorrowBookListResource {

  private Collection<BorrowBookResource> borrowBookResourceList;

  public EmbeddedBorrowBookListResource() {}

  public EmbeddedBorrowBookListResource(Collection<BorrowBookResource> books) {
    this.borrowBookResourceList = books;
  }

  public Collection<BorrowBookResource> getBorrowBookResourceList() {
    return borrowBookResourceList;
  }

  public void setBorrowBookResourceList(Collection<BorrowBookResource> books) {
    this.borrowBookResourceList = books;
  }
}
