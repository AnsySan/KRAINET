package com.ansysan.project.authentication_service.controller;

import com.ansysan.project.authentication_service.dto.request.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.response.UserResponse;
import com.ansysan.project.authentication_service.service.authentication.AuthenticationService;
import com.ansysan.project.authentication_service.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("user");
        request.setPassword("password");
        request.setEmail("user@example.com");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setUsername("user");
        response.setPassword("password");
        response.setEmail("user@example.com");
        response.setPhone("+123123123123");

        Mockito.when(authenticationService.registration(any(RegistrationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/v1/api/user/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setUsername("admin");

        Mockito.when(userService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/v1/api/user/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("admin"));
    }

}
