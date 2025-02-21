package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.AuthorRequest;
import com.bookstore.app.dto.response.AuthorResponse;
import com.bookstore.app.entity.Author;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.AuthorRepository;
import com.bookstore.app.service.AuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    private ModelMapper modelMapper;

    public List<AuthorResponse> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(author -> modelMapper.map(author, AuthorResponse.class))
                .toList();
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(author -> modelMapper.map(author, AuthorResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest authorRequest) {
        authorRepository.findByName(authorRequest.getName())
                .ifPresent(author -> {
                    throw new ResourceAlreadyExistsException("Author already exists");
                });
        Author savedAuthor = authorRepository.save(modelMapper.map(authorRequest, Author.class));
        return modelMapper.map(savedAuthor, AuthorResponse.class);
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        existingAuthor.setName(authorRequest.getName());
        return modelMapper.map(existingAuthor, AuthorResponse.class);
    }

    @Override
    public void deleteAuthorById(Long id) {
        authorRepository.findById(id)
                .ifPresentOrElse(author -> authorRepository.deleteById(id),
                        () -> {
                    throw new ResourceNotFoundException("Author not found");
                        });
    }
}
