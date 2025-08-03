package com.ansysan.project.authentication_service.validator;

import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.exception.DataValidationException;
import com.ansysan.project.authentication_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User validateUserExistence(Long userId) {
        var optional = userRepository.findById(userId);
        return optional.orElseThrow(() -> {
            var message = String.format("a user with %d does not exist", userId);
            return new DataValidationException(message);
        });
    }

}
