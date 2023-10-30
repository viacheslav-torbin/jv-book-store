package org.bookstore.service;

import org.bookstore.dto.user.RegisterUserRequest;
import org.bookstore.dto.user.UserResponseDto;
import org.bookstore.exceptions.RegistrationException;

public interface UserService {
    UserResponseDto register(RegisterUserRequest request) throws RegistrationException;
}
