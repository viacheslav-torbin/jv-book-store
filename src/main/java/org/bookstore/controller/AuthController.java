package org.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.user.RegisterUserRequest;
import org.bookstore.dto.user.UserLoginRequestDto;
import org.bookstore.dto.user.UserLoginResponseDto;
import org.bookstore.dto.user.UserResponseDto;
import org.bookstore.exceptions.RegistrationException;
import org.bookstore.security.AuthenticationService;
import org.bookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid RegisterUserRequest request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
