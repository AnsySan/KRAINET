package com.ansysan.project.authentication_service.service.user;

import com.ansysan.project.authentication_service.dto.response.UserResponse;
import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.exception.NotFoundException;
import com.ansysan.project.authentication_service.mapper.UserMapper;
import com.ansysan.project.authentication_service.repository.UserRepository;
import com.ansysan.project.authentication_service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponse findByUsername(String email) {
        User user = userRepository.findByUsername(email).orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        log.debug("Deleting user: {}", id);
        userValidator.validateUserExistence(id);
        userRepository.deleteById(id);
        log.debug("Deleted user: {}", id);
    }

    @Override
    public UserResponse update(Long id, UserResponse userResponse) {
        log.debug("update");
        User user = userValidator.validateUserExistence(id);
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        return userMapper.toDto(userRepository.save(user));
    }

}
