package com.ansysan.project.authentication_service.service.authentication;

import com.ansysan.project.authentication_service.dto.request.LoginRequest;
import com.ansysan.project.authentication_service.dto.request.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.response.TokenResponse;
import com.ansysan.project.authentication_service.dto.response.UserResponse;
import com.ansysan.project.authentication_service.entity.enums.Role;
import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.mapper.UserMapper;
import com.ansysan.project.authentication_service.repository.TokenRepository;
import com.ansysan.project.authentication_service.repository.UserRepository;
import com.ansysan.project.authentication_service.service.UserDetailsServiceImpl;
import com.ansysan.project.authentication_service.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;
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

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        return jwtService.generateToken(userDetails);
    }

    @Override
    public UserResponse registration(RegistrationRequest registrationRequestDto) {
        log.debug("Registration request: {}", registrationRequestDto);
        User user = User.builder()
                .username(registrationRequestDto.getUsername())
                .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .email(registrationRequestDto.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        log.debug("Refresh token: {}", refreshToken);
        return jwtService.regenerateToken(refreshToken);
    }
}