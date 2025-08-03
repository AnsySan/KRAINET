package com.ansysan.project.authentication_service.service.auth;

import com.ansysan.project.authentication_service.dto.request.LoginRequest;
import com.ansysan.project.authentication_service.dto.request.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.response.TokenResponse;
import com.ansysan.project.authentication_service.dto.response.UserResponse;
import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.entity.enums.Role;
import com.ansysan.project.authentication_service.mapper.UserMapper;
import com.ansysan.project.authentication_service.repository.TokenRepository;
import com.ansysan.project.authentication_service.repository.UserRepository;
import com.ansysan.project.authentication_service.service.authentication.AuthenticationServiceImpl;
import com.ansysan.project.authentication_service.service.jwt.JwtService;
import com.ansysan.project.authentication_service.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private TokenResponse tokenResponse;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        tokenResponse = new TokenResponse("accessToken", "refreshToken", 1L);
        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("testuser");
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(tokenResponse);

        TokenResponse result = authenticationService.login(loginRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password")
        );

        assertThat(result).isEqualTo(tokenResponse);
    }

    @Test
    void registration_ShouldSaveUserAndReturnResponse() {
        RegistrationRequest dto = new RegistrationRequest();
        dto.setUsername("newuser");
        dto.setPassword("password");
        dto.setEmail("new@example.com");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponse);

        UserResponse result = authenticationService.registration(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("newuser");
        assertThat(savedUser.getEmail()).isEqualTo("new@example.com");
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);

        assertThat(result).isEqualTo(userResponse);
    }

    @Test
    void refreshToken_ShouldCallJwtServiceAndReturnNewToken() {
        String oldToken = "refreshToken123";
        when(jwtService.regenerateToken(oldToken)).thenReturn(tokenResponse);

        TokenResponse result = authenticationService.refreshToken(oldToken);

        assertThat(result).isEqualTo(tokenResponse);
        verify(jwtService).regenerateToken(oldToken);
    }
}