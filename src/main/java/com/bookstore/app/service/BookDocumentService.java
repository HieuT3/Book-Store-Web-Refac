package com.bookstore.app.service;

import com.bookstore.app.elasticsearch.BookDocument;

import java.util.List;

public interface BookDocumentService {
    List<BookDocument> getAll();
    BookDocument getBookDocumentById(String id);
    BookDocument addBookDocument(BookDocument bookDocument);
    List<BookDocument> saveAll();
    BookDocument updateBookDocument(String id, BookDocument bookDocument);
    void deleteBookDocumentById(String id);
    void deleteAll();
}
