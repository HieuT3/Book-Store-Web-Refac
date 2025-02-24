package com.bookstore.app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
     Long bookId;
     String title;
     String description;
     String isbn;
     String imageURL;
     double price;
     LocalDate publishedDate;
    Set<AuthorResponse> authors;
    Set<CategoryResponse> categories;
}
