package org.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.bookstore.dto.user.RegisterUserRequest;
import org.bookstore.dto.user.UserResponseDto;
import org.bookstore.exceptions.RegistrationException;
import org.bookstore.mapper.UserMapper;
import org.bookstore.model.User;
import org.bookstore.repository.UserRepository;
import org.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(RegisterUserRequest request) throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setShippingAddress(request.getShippingAddress());
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
}
