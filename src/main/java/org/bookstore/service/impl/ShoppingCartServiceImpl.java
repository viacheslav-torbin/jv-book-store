package org.bookstore.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.shoppingcart.CartItemDto;
import org.bookstore.dto.shoppingcart.CartItemRequestDto;
import org.bookstore.dto.shoppingcart.CartItemUpdateDto;
import org.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.bookstore.exceptions.EntityNotFoundException;
import org.bookstore.mapper.CartItemMapper;
import org.bookstore.mapper.ShoppingCartMapper;
import org.bookstore.model.CartItem;
import org.bookstore.model.ShoppingCart;
import org.bookstore.model.User;
import org.bookstore.repository.BookRepository;
import org.bookstore.repository.CartItemRepository;
import org.bookstore.repository.ShoppingCartRepository;
import org.bookstore.repository.UserRepository;
import org.bookstore.service.ShoppingCartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartMapper.toDto(getUserCart());
    }

    @Override
    public CartItemDto addCartItem(CartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(requestDto.quantity());
        cartItem.setBook(bookRepository.findById(requestDto.bookId()).orElseThrow(()
                -> new EntityNotFoundException("Can't find book with id: " + requestDto.bookId())));
        cartItem.setShoppingCart(getUserCart());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Transactional
    @Override
    public CartItemDto updateCartItem(Long itemId, CartItemUpdateDto updateDto) {
        ShoppingCart shoppingCart = getUserCart();
        CartItem cartItem = findCartItem(
                itemId, shoppingCart.getId(), shoppingCart.getUser().getId());
        cartItem.setQuantity(updateDto.quantity());
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void removeCartItem(Long itemId) {
        ShoppingCart shoppingCart = getUserCart();
        CartItem cartItem = findCartItem(
                itemId, shoppingCart.getId(), shoppingCart.getUser().getId());
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getUserCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new EntityNotFoundException("Can't find user by email"));
        return shoppingCartRepository.findByUserId(user.getId()).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user);
            return shoppingCartRepository.save(newCart);
        });
    }

    private CartItem findCartItem(Long itemId, Long shoppingCartId, Long userId) {
        return cartItemRepository.findByIdAndShoppingCartId(
                itemId, shoppingCartId).orElseThrow(
                        () -> new EntityNotFoundException(
                "Cant find cart item by id: %s for user id: %s"
                        .formatted(itemId, userId)));
    }
}
