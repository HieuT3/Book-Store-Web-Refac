package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.BookRequest;
import com.bookstore.app.dto.response.BookResponse;
import com.bookstore.app.dto.response.PageResponse;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;
    AuthorRepository authorRepository;
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    @Cacheable(value = "books", key = "'all'")
    @Override
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Cacheable(value = "books", key = "T(String).format('pagination::page=%d::limit=%d', #page, #limit)")
    @Override
    public PageResponse<BookResponse> getBooksPagination(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<BookResponse> responses = bookRepository.findAll(pageable)
                .map(book -> modelMapper.map(book, BookResponse.class));
        return PageResponse.<BookResponse>builder()
                .content(responses.getContent())
                .number(responses.getNumber())
                .size(responses.getSize())
                .totalElements(responses.getTotalElements())
                .totalPages(responses.getTotalPages())
                .numberOfElements(responses.getNumberOfElements())
                .first(responses.isFirst())
                .last(responses.isLast())
                .empty(responses.isEmpty())
                .build();
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
    public List<BookResponse> getBooksFeatured() {
        return bookRepository.findAll()
                .stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .toList();
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

    @Cacheable(value = "books", key = "'category::' + #categoryId")
    @Override
    public List<BookResponse> getBooksByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return bookRepository.findByCategories(Set.of(category))
                .stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Cacheable(value = "books", key = "'featured::category::' + #categoryId")
    @Override
    public List<BookResponse> getBooksFeaturedByCategoryId(Long categoryId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return bookRepository.findBooksFeaturedByCategory(categoryId, pageable)
                .stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<BookResponse> searchByTitle(String title) {
        return List.of();
    }

    @Override
    public Long countBooksByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return bookRepository.countBooksByCategories(Set.of(category));
    }

    private void setBook(Book book, BookRequest bookRequest) {
        Set<Author> authorsSet = new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorsId()));
        Set<Category> categoriesSet = new HashSet<>(categoryRepository.findAllById(bookRequest.getCategoriesId()));
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setIsbn(bookRequest.getIsbn());
        book.setSmallImageUrl(bookRequest.getSmallImageUrl());
        book.setMediumImageUrl(bookRequest.getMediumImageUrl());
        book.setLargeImageUrl(bookRequest.getLargeImageUrl());
        book.setPrice(bookRequest.getPrice());
        book.setPublishedDate(bookRequest.getPublishedDate());
        book.setAuthors(authorsSet);
        book.setCategories(categoriesSet);
    }
}
