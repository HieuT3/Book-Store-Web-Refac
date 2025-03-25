package com.bookstore.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @NotBlank
    String shippingAddress;

    @NotBlank
    String recipientName;

    @NotBlank
    String recipientPhone;

    @NotBlank
    String paymentMethod;

    @NotNull
    double shippingCost;

}
