package com.bookstore.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "book", referencedColumnName = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "user_id")
    private User user;

    @Column(nullable = false, length = 100000)
    private String comment;
    @CreationTimestamp
    private LocalDateTime reviewTime;
}
