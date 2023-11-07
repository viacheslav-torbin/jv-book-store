package org.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bookstore.constraint.FieldMatch;

@FieldMatch(fields = {"password", "repeatPassword"}, message = "Passwords are not equal")
public record UserRegistrationRequestDto(
        @Email
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String email,
        @NotBlank
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String password,
        @NotBlank
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String repeatPassword,
        @NotBlank(message = "First name must not be null or empty")
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String firstName,
        @NotBlank(message = "Last name must not be null or empty")
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String lastName,
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String shippingAddress) {

}
