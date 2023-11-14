package org.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.shoppingcart.CartItemRequestDto;
import org.bookstore.dto.shoppingcart.CartItemUpdateDto;
import org.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.bookstore.model.User;
import org.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final ShoppingCartService cartService;

    @Operation(summary = "Get the shopping cart of a user")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.getCart(user.getId());
    }

    @Operation(summary = "Add a book to the shopping cart", description = """
                    Add a new book or update the quantity of existing in the cart of user""")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto addBook(Authentication authentication,
                                   @RequestBody @Valid CartItemRequestDto cartItem) {
        User user = (User) authentication.getPrincipal();
        return cartService.addBook(user.getId(), cartItem);
    }

    @Operation(summary = "Update the number of books in the cart", description = """
                    Update the number of existing books in the cart of a logged-in user.
                    Parameters : cart item id and new quantity""")
    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto updateBookQuantity(Authentication authentication,
                                              @PathVariable @Positive Long cartItemId,
                                              @RequestBody @Valid CartItemUpdateDto item) {
        User user = (User) authentication.getPrincipal();
        return cartService.updateQuantity(user.getId(), cartItemId, item);
    }

    @Operation(summary = "Delete book from the cart of user", description = """
                    Delete book from the cart of a user.
                    Parameter: id of cart item""")
    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookFromCart(Authentication authentication,
                                   @PathVariable @Positive Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        cartService.deleteBook(user.getId(), cartItemId);
    }
}
