package com.ansysan.project.authentication_service.controller;

import com.ansysan.project.authentication_service.dto.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.UserDto;
import com.ansysan.project.authentication_service.service.AuthenticationService;
import com.ansysan.project.authentication_service.service.UserService;
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
@RequestMapping("v1/api/admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
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
    public UserDto create(@RequestBody @Valid RegistrationRequest registrationRequestDto) {
        log.info("Create user: {}", registrationRequestDto);
        return authenticationService.registration(registrationRequestDto);
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
    public UserDto getUser(@PathVariable("userId") Long userId) {
        log.info("Get user: {}", userId);
        return userService.getUserById(userId);
    }

}