package org.bookstore.repository;

import java.util.Optional;
import org.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    CartItem findCartItemByIdAndShoppingCartId(Long itemId, Long shoppingCartId);
}
