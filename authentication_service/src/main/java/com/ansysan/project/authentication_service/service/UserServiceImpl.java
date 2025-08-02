package com.ansysan.project.authentication_service.service;

import com.ansysan.project.authentication_service.dto.UserDto;
import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.exception.NotFoundException;
import com.ansysan.project.authentication_service.mapper.UserMapper;
import com.ansysan.project.authentication_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("loadUserByUsername");
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found" + username));
    }
    @Override
    public boolean existsByUsername(String username) {
        log.debug("exists by username {}", username);
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("exists by Email {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto getUserById(long id) {
        log.debug("get user by id {}", id);
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public void deleteUser(long id) {
        log.debug("Deleting user: {}", id);
        userRepository.deleteById(id);
        log.debug("Deleted user: {}", id);
    }

    @Override
    public User findByUsername(String username) {
        log.debug("findByUsername {}", username);
        return userRepository.findByUsername(username).orElseThrow(() ->new  NotFoundException("User not found" + username));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        log.debug("update");
        User user = findById(id);
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void banUserByIds(long id) {
        log.debug("Banning user: {}", id);
        User user = findById(id);
        user.setIsBanned(true);
        userRepository.save(user);
        log.debug("Banned user: {}", id);
    }

    public User findById(Long id) {
        log.debug("find by user: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found" + id));
    }
}
