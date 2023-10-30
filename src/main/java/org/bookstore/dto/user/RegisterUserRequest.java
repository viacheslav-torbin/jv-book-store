package org.bookstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bookstore.constraint.FieldMatch;

@FieldMatch(message = "Passwords are not equal")
@Data
public class RegisterUserRequest {
    @NotBlank
    @Size(min = 4, max = 50)
    private String email;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    @NotBlank
    @Size(min = 6, max = 20)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
