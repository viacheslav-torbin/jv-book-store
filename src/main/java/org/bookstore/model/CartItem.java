package org.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "shopping_cart_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ShoppingCart shoppingCart;

    @JoinColumn(name = "book_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
    private Book book;

    @Column(nullable = false)
    private int quantity;
}
