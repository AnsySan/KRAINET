package com.ansysan.project.authentication_service.service;

import com.ansysan.project.authentication_service.dto.UserDto;
import com.ansysan.project.authentication_service.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDetails loadUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    UserDto getUserById(long id);

    void deleteUser(long id);

    User findByUsername(String username);

    UserDto update(Long id, UserDto userDto);

    void banUserByIds(long id);

}