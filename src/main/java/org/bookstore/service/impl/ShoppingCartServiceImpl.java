package org.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.shoppingcart.CartItemDto;
import org.bookstore.dto.shoppingcart.CartItemRequestDto;
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
        return shoppingCartMapper.toDto(getCart());
    }

    @Transactional
    @Override
    public CartItemDto addCartItem(CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getCart();
        Optional<CartItem> cartItemFromDB = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), requestDto.bookId());
        if (cartItemFromDB.isPresent()) {
            CartItem cartItem = cartItemFromDB.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
            return cartItemMapper.toDto(cartItemRepository.save(cartItem));
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setShoppingCart(shoppingCart);
        newCartItem.setBook(bookRepository.getReferenceById(requestDto.bookId()));
        newCartItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(newCartItem));
    }

    private ShoppingCart getCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No user with such id"));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCart;
    }

    @Override
    public CartItemDto updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findCartItemByIdAndShoppingCartId(cartItemId,
                getShoppingCart().userId());
        cartItem.setQuantity(quantity);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void removeCartItem(Long itemId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        CartItem cartItem = findCartItem(itemId, shoppingCart.getId());
        cartItemRepository.delete(cartItem);
    }

    private CartItem findCartItem(Long itemId, Long shoppingCartId) {
        return cartItemRepository.findCartItemByIdAndShoppingCartId(itemId, shoppingCartId);
    }
}
