package org.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.shoppingcart.CartItemDto;
import org.bookstore.dto.shoppingcart.CartItemRequestDto;
import org.bookstore.dto.shoppingcart.CartItemUpdateDto;
import org.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing user`s shopping carts")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get shopping cart",
            description = "Retrieve user's shopping cart")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add book to cart",
            description = "Add book to the shopping cart")
    public CartItemDto addBookToCart(@RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addCartItem(requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity",
            description = "Update quantity of a book in the shopping cart")
    public CartItemDto updateBookQuantity(@PathVariable @Positive Long cartItemId,
                                          @RequestBody @Valid CartItemUpdateDto updateDto) {
        return shoppingCartService.updateCartItem(cartItemId, updateDto.quantity());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete book from cart",
            description = "Remove a book from the shopping cart")
    public void deleteBookFromCart(@PathVariable @Positive Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);
    }
}
