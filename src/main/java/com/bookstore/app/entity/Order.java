package com.bookstore.app.entity;

import com.bookstore.app.constant.OrderStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false, length = 50)
    private String recipientName;

    @Column(nullable = false, length = 10)
    private String recipientPhone;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false, precision = 2)
    private double shippingCost;

    @Column(nullable = false)
    private int bookCopies;

    @Column(nullable = false, precision = 2)
    private double total;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatus = OrderStatusType.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();
}
