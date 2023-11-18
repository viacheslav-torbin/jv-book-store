package org.bookstore.service;

import org.bookstore.dto.shoppingcart.CartItemRequestDto;
import org.bookstore.dto.shoppingcart.CartItemUpdateDto;
import org.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.bookstore.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartDto getCart(Long userId);

    ShoppingCartDto addBook(Long userId, CartItemRequestDto item);

    ShoppingCartDto updateQuantity(Long userId, Long cartItemId, CartItemUpdateDto itemDto);

    void deleteBook(Long userId, Long itemId);
}
