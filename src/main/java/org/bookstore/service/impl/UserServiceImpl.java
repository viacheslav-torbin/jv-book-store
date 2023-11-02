package org.bookstore.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.user.UserRegistrationRequestDto;
import org.bookstore.dto.user.UserResponseDto;
import org.bookstore.exceptions.RegistrationException;
import org.bookstore.mapper.UserMapper;
import org.bookstore.model.Role;
import org.bookstore.model.RoleName;
import org.bookstore.model.User;
import org.bookstore.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private Role userRole;

    @PostConstruct
    public void init() {
        userRole = roleRepository.findRoleByRoleName(RoleName.ROLE_USER);
    }

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Unable to complete registration");
        }
        User savedUser = userRepository.save(userMapper.toUser(request));
        return userMapper.toUserResponse(savedUser);
    }
}
