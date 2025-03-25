package com.bookstore.app.utils;

import com.bookstore.app.elasticsearch.BookDocument;
import com.bookstore.app.entity.Author;
import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Category;

public class BookMapper {
    public static BookDocument convertToBookDocument(Book book) {
        BookDocument bookDocument = new BookDocument();
        bookDocument.setBookId(book.getBookId().toString());
        bookDocument.setTitle(book.getTitle());
        bookDocument.setDescription(book.getDescription());
        bookDocument.setSmallImageUrl(book.getSmallImageUrl());
        bookDocument.setPublishedDate(book.getPublishedDate());
        bookDocument.setPrice(book.getPrice());
        bookDocument.setAuthors(book.getAuthors().stream().map(Author::getName).toList());
        bookDocument.setCategories(book.getCategories().stream().map(Category::getName).toList());
        return bookDocument;
    }
}
