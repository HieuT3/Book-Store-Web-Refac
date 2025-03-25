package com.bookstore.app.service.impl;

import com.bookstore.app.elasticsearch.BookDocument;
import com.bookstore.app.elasticsearch.BookDocumentRepository;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.BookRepository;
import com.bookstore.app.service.BookDocumentService;
import com.bookstore.app.utils.BookMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookDocumentServiceImpl implements BookDocumentService {

    BookRepository bookRepository;
    BookDocumentRepository bookDocumentRepository;

    @Override
    public List<BookDocument> getAll() {
        return StreamSupport.stream(bookDocumentRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public BookDocument getBookDocumentById(String id) {
        return bookDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Book document with id %s not found", id)
                ));
    }

    @Override
    public BookDocument addBookDocument(BookDocument bookDocument) {
        List<BookDocument> bookDocuments = bookDocumentRepository.findByTitle(bookDocument.getTitle());
        if (!bookDocuments.isEmpty())
            throw new ResourceAlreadyExistsException(
                    String.format("Book document with title %s already exists", bookDocument.getTitle())
            );
        return bookDocumentRepository.save(bookDocument);
    }

    @Override
    public BookDocument updateBookDocument(String id, BookDocument bookDocument) {
        BookDocument existingBookDocument = bookDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Book document with id %s not found", id)
                ));
        bookDocument.setBookId(existingBookDocument.getBookId());
        return bookDocumentRepository.save(bookDocument);
    }

    @Override
    public void deleteBookDocumentById(String id) {
        BookDocument bookDocument = bookDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Book document with id %s not found", id)
                ));
        bookDocumentRepository.delete(bookDocument);
    }

    @Override
    public List<BookDocument> saveAll() {
        List<BookDocument> books = bookRepository.findAll()
                .stream()
                .map(BookMapper::convertToBookDocument)
                .toList();
        return StreamSupport.stream(bookDocumentRepository.saveAll(books).spliterator(), false)
                .toList();
    }

    @Override
    public void deleteAll() {
        bookDocumentRepository.deleteAll();
    }
}
