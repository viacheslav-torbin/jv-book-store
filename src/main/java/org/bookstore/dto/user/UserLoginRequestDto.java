package org.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Email
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String email,
        @NotBlank(message = "Password must not be null or empty")
        @Size(max = 255, message = "Maximum allowed size 255 characters")
        String password) {
}
