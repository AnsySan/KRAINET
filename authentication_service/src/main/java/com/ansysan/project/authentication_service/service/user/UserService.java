package com.ansysan.project.authentication_service.service.user;

import com.ansysan.project.authentication_service.dto.response.UserResponse;
import com.ansysan.project.authentication_service.entity.User;

import java.util.List;

public interface UserService  {

    UserResponse findById(Long id);

    List<User> findAll();

    void deleteUser(long id);

    UserResponse update(Long id, UserResponse userResponse);

    void banUserByIds(long id);

}