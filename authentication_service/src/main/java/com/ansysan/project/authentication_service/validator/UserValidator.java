package com.ansysan.project.authentication_service.validator;

import com.ansysan.project.authentication_service.entity.User;

public interface UserValidator {
    User validateUserExistence(Long userId);

}
