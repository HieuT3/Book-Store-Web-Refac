package com.bookstore.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 2)
    private double subTotal;

    @ManyToOne
    @JoinColumn(referencedColumnName = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(referencedColumnName = "order_id")
    private Order order;
}
