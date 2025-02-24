package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.BookRequest;
import com.bookstore.app.dto.response.BookResponse;
import com.bookstore.app.entity.Author;
import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Category;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.AuthorRepository;
import com.bookstore.app.repository.BookRepository;
import com.bookstore.app.repository.CategoryRepository;
import com.bookstore.app.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;
    AuthorRepository authorRepository;
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    @Override
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .toList();
    }

    @Override
    public BookResponse getBookById(Long id) {
        return modelMapper.map(
                bookRepository.findById(id)
                        .orElseThrow(
                                () ->  new ResourceNotFoundException("Book not found with id: " + id)
                        ),
                BookResponse.class
        );
    }

    @Override
    public BookResponse getBookByTitle(String title) {
        return modelMapper.map(
                bookRepository.findByTitle(title).orElseThrow(
                        () -> new ResourceNotFoundException("Book not found with title: " + title)
                ),
                BookResponse.class
        );
    }

    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        if(bookRepository.findByTitle(bookRequest.getTitle()).isPresent())
            throw new ResourceAlreadyExistsException("Book already exists with title: " + bookRequest.getTitle());
        Book savedBook = new Book();
        setBook(savedBook, bookRequest);
        return modelMapper.map(bookRepository.save(savedBook), BookResponse.class);
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book not found with id: " + id)
        );
        setBook(existingBook, bookRequest);
        return modelMapper.map(bookRepository.save(existingBook), BookResponse.class);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.findById(id)
                .ifPresentOrElse(
                        book -> bookRepository.deleteById(id),
                        () -> {
                            throw new ResourceNotFoundException("Book not found with id: " + id);
                        }
                );
    }

    @Override
    public List<BookResponse> getAllByAuthorId(Long authorId) {
        return List.of();
    }

    @Override
    public List<BookResponse> getAllByCategoryId(Long categoryId) {
        return List.of();
    }

    @Override
    public List<BookResponse> searchByTitle(String title) {
        return List.of();
    }

    private void setBook(Book book, BookRequest bookRequest) {
        Set<Author> authorsSet = new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorsId()));
        Set<Category> categoriesSet = new HashSet<>(categoryRepository.findAllById(bookRequest.getCategoriesId()));
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setIsbn(bookRequest.getIsbn());
        book.setImageURL(bookRequest.getImageURL());
        book.setPrice(bookRequest.getPrice());
        book.setPublishedDate(bookRequest.getPublishedDate());
        book.setAuthors(authorsSet);
        book.setCategories(categoriesSet);
    }
}
