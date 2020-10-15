package com.example.library.client.web;

public class BorrowBookListResource {

  private EmbeddedBorrowBookListResource _embedded;

  public BorrowBookListResource() {}

  public BorrowBookListResource(EmbeddedBorrowBookListResource embeddedBorrowBookListResource) {
    this._embedded = embeddedBorrowBookListResource;
  }

  public EmbeddedBorrowBookListResource get_embedded() {
    return _embedded;
  }

  public void set_embedded(EmbeddedBorrowBookListResource _embedded) {
    this._embedded = _embedded;
  }
}
