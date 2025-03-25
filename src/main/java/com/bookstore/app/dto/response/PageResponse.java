package com.bookstore.app.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    List<T> content;
    int number;
    int size;
    long totalElements;
    int totalPages;
    int numberOfElements;
    boolean last;
    boolean first;
    boolean empty;

}
