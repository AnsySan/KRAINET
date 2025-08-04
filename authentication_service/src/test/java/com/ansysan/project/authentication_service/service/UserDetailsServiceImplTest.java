package com.ansysan.project.authentication_service.service;

import com.ansysan.project.authentication_service.dto.request.LoginRequest;
import com.ansysan.project.authentication_service.dto.response.TokenResponse;
import com.ansysan.project.authentication_service.service.authentication.AuthenticationServiceImpl;
import com.ansysan.project.authentication_service.service.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void login_ShouldAuthenticateAndReturnTokenResponse() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "testuser",
                "encodedPassword",
                Collections.singletonList((GrantedAuthority) () -> "ROLE_USER")
        );

        TokenResponse expectedTokenResponse = new TokenResponse("access-token", "refresh-token");

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(expectedTokenResponse);

        TokenResponse actualTokenResponse = authenticationService.login(loginRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password123")
        );
        verify(userDetailsService).loadUserByUsername("testuser");
        verify(jwtService).generateToken(userDetails);

        assertThat(actualTokenResponse).isNotNull();
        assertThat(actualTokenResponse.getAccessToken()).isEqualTo("access-token");
        assertThat(actualTokenResponse.getRefreshToken()).isEqualTo("refresh-token");
    }
}
