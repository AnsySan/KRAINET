package com.ansysan.project.authentication_service.controller;

import com.ansysan.project.authentication_service.dto.request.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.response.UserResponse;
import com.ansysan.project.authentication_service.service.authentication.AuthenticationService;
import com.ansysan.project.authentication_service.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("v1/api/user")
@PreAuthorize("hasRole('USER')")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "create user",
            description = "Creates a new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration user",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegistrationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "user successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid create")
            }
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid RegistrationRequest registrationRequestDto) {
        log.info("Create user: {}", registrationRequestDto);
        return authenticationService.registration(registrationRequestDto);
    }

    @Operation(
            summary = "Update user",
            description = "Updates a user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update user",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "user successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid create")
            }

    )
    @PatchMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@PathVariable("userId") Long userId, @RequestBody @Valid UserResponse userResponseDto) {
        log.info("Update user: {}", userResponseDto);
        return userService.update(userId, userResponseDto);
    }

    @Operation(
            summary = "Delete user",
            description = "Deleted a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @Parameter @PathVariable("userId") Long userId) {
        log.info("Delete user: {}", userId);
        userService.deleteUser(userId);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Get user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("get/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable("userId") Long userId) {
        log.info("Get user: {}", userId);
        return userService.findById(userId);
    }
}