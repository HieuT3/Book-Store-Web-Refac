package com.bookstore.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    @NotBlank
    String title;

    @NotBlank
    String description;

    @NotBlank
    String isbn;

    @NotBlank
    String smallImageUrl;

    @NotBlank
    String mediumImageUrl;

    @NotBlank
    String largeImageUrl;

    @NotNull
    double price;

    @NotNull
    LocalDate publishedDate;

    @NotEmpty
    Set<Long> authorsId;

    @NotEmpty
    Set<Long> categoriesId;
}
