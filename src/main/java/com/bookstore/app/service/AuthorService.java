package com.bookstore.app.service;

import com.bookstore.app.dto.request.AuthorRequest;
import com.bookstore.app.dto.response.AuthorResponse;

import java.util.List;

public interface AuthorService {
     List<AuthorResponse> getAll();
     AuthorResponse getAuthorById(Long id);
     AuthorResponse createAuthor(AuthorRequest authorRequest);
     AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest);
     void deleteAuthorById(Long id);
}
