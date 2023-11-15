package org.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.shoppingcart.CartItemRequestDto;
import org.bookstore.dto.shoppingcart.CartItemUpdateDto;
import org.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.bookstore.exceptions.EntityNotFoundException;
import org.bookstore.mapper.CartItemMapper;
import org.bookstore.mapper.ShoppingCartMapper;
import org.bookstore.model.Book;
import org.bookstore.model.CartItem;
import org.bookstore.model.ShoppingCart;
import org.bookstore.model.User;
import org.bookstore.repository.BookRepository;
import org.bookstore.repository.CartItemRepository;
import org.bookstore.repository.ShoppingCartRepository;
import org.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Transactional
    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto getCart(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId));
    }

    @Transactional
    @Override
    public ShoppingCartDto addBook(Long userId, CartItemRequestDto itemDto) {
        Book book = bookRepository.findById(itemDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Book with id: %d is not found", itemDto.bookId())
                ));
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        Set<CartItem> cartItems = cart.getCartItems();
        for (CartItem item : cartItems) {
            if (Objects.equals(item.getBook().getId(), itemDto.bookId())) {
                item.setQuantity(item.getQuantity() + itemDto.quantity());
                shoppingCartRepository.save(cart);
                return shoppingCartMapper.toDto(cart);
            }
        }
        addCartItemToShoppingCart(itemDto, book, cart);
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public ShoppingCartDto updateQuantity(Long userId,
                                          Long cartItemId, CartItemUpdateDto itemDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, cart.getId())
                .map(item -> {
                    item.setQuantity(itemDto.quantity());
                    return item;
                }).orElseThrow(() -> new EntityNotFoundException(
                        String.format("No cart item with id: %d for user: %d", cartItemId, userId)
                ));
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public void deleteBook(Long userId, Long itemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(itemId, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No such cartItem with id %d in shopping cart", itemId)
                ));
        shoppingCart.removeItemFromCart(cartItem);
    }

    private void addCartItemToShoppingCart(CartItemRequestDto itemDto,
                                           Book book,
                                           ShoppingCart cart) {
        CartItem cartItem = cartItemMapper.toCartItem(itemDto);
        cartItem.setBook(book);
        cart.addItemToCart(cartItem);
    }
}
