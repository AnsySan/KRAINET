package com.ansysan.project.authentication_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private long id;

    @NotBlank
    @Size(max = 64, message = "username should be less than 65 symbols")
    private String username;

    @NotBlank
    @Email
    @Size(max = 64, message = "email should be less than 65 symbols")
    private String email;

    @NotBlank
    @Size(max = 32, message = "phone should be less than 32 symbols")
    private String phone;

    @NotBlank
    @Size(max = 128, message = "password should be less than 129 symbols")
    private String password;

    @NotBlank
    private boolean isBanned;

}
