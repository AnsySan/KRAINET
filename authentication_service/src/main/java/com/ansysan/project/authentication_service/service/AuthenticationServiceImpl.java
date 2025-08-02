package com.ansysan.project.authentication_service.service;

import com.ansysan.project.authentication_service.dto.LoginRequest;
import com.ansysan.project.authentication_service.dto.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.TokenResponse;
import com.ansysan.project.authentication_service.dto.UserDto;
import com.ansysan.project.authentication_service.entity.Role;
import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.mapper.UserMapper;
import com.ansysan.project.authentication_service.repository.TokenRepository;
import com.ansysan.project.authentication_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;


    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        log.debug("Login request: {}", loginRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        User user = (User) userService.loadUserByUsername(loginRequest.getUsername());

        log.debug("User: {}", user);
        return jwtService.generateToken(user);
    }

    @Override
    public UserDto registration(RegistrationRequest registrationRequestDto) {
        log.debug("Registration request: {}", registrationRequestDto);
        User user = User.builder()
                .username(registrationRequestDto.getUsername())
                .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .email(registrationRequestDto.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        log.debug("User: {}", user);
        return userMapper.toDto(user);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        log.debug("Refresh token: {}", refreshToken);
        return jwtService.regenerateToken(refreshToken);
    }
}