package com.ansysan.project.authentication_service.service.user;

import com.ansysan.project.authentication_service.dto.response.UserResponse;
import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.exception.NotFoundException;
import com.ansysan.project.authentication_service.mapper.UserMapper;
import com.ansysan.project.authentication_service.repository.UserRepository;
import com.ansysan.project.authentication_service.service.UserDetailsServiceImpl;
import com.ansysan.project.authentication_service.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setIsBanned(false);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("testuser");
    }


    @Test
    void findById_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse result = userService.findById(1L);

        assertThat(result).isEqualTo(userResponse);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void findAll_ShouldReturnUserList() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertThat(result).hasSize(1).contains(user);
        verify(userRepository).findAll();
    }

    @Test
    void deleteUser_ShouldCallValidatorAndRepository() {
        when(userValidator.validateUserExistence(1L)).thenReturn(user);

        userService.deleteUser(1L);

        verify(userValidator).validateUserExistence(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void update_ShouldUpdateUserAndReturnDto() {
        when(userValidator.validateUserExistence(1L)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse result = userService.update(1L, userResponse);

        assertThat(result.getUsername()).isEqualTo("testuser");
    }

}