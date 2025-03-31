package com.bookstore.app.dto.response;

import com.bookstore.app.constant.OrderStatusType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long orderId;
    UserResponse user;
    LocalDateTime orderDate;
    String recipientName;
    String recipientEmail;
    String recipientPhone;
    String shippingAddress;
    String recipientCity;
    String recipientPostalCode;
    String recipientCountry;
    String shippingMethod;
    double shippingCost;
    String paymentMethod;
    int bookCopies;
    double total;
    OrderStatusType orderStatus;
    Set<OrderDetailResponse> orderDetails;
}
