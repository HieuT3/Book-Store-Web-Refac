package com.bookstore.app.dto.response;

import com.bookstore.app.entity.Book;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Long orderDetailId;
    int quantity;
    double subTotal;
    Book books;
}
