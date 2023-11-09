package org.bookstore.service;

import org.bookstore.dto.shoppingcart.CartItemDto;
import org.bookstore.dto.shoppingcart.CartItemRequestDto;
import org.bookstore.dto.shoppingcart.CartItemUpdateDto;
import org.bookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemDto addCartItem(CartItemRequestDto requestDto);

    CartItemDto updateCartItem(Long itemId, CartItemUpdateDto updateDto);

    void removeCartItem(Long itemId);
}
