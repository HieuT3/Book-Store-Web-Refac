package com.bookstore.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @NotBlank
    String recipientName;

    @NotBlank
    String recipientEmail;

    @NotBlank
    String recipientPhone;

    @NotBlank
    String shippingAddress;

    @NotBlank
    String recipientCity;

    @NotBlank
    String recipientPostalCode;

    @NotBlank
    String recipientCountry;

    @NotBlank
    String shippingMethod;

    @NotNull
    double shippingCost;

    @NotBlank
    String paymentMethod;

}
