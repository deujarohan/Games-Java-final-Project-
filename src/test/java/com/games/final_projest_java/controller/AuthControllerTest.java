package com.games.final_projest_java.controller;

import com.games.final_projest_java.dto.RegistrationDto;
import com.games.final_projest_java.model.User;
import com.games.final_projest_java.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(com.games.final_projest_java.config.SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void loginPageShouldReturn200() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void registerPageShouldReturn200() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }

    @Test
    void registerWithValidDataShouldRedirectToLogin() throws Exception {
        User mockUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();
        when(userService.register(any(RegistrationDto.class))).thenReturn(mockUser);

        mockMvc.perform(post("/register").with(csrf())
                .param("username", "testuser")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered=true"));
    }

    @Test
    void registerWithBlankUsernameShouldShowErrors() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("username", "")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("registrationDto", "username"));
    }

    @Test
    void registerWithInvalidEmailShouldShowErrors() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("username", "validuser")
                .param("email", "not-an-email")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("registrationDto", "email"));
    }

    @Test
    void registerWithShortPasswordShouldShowErrors() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("username", "validuser")
                .param("email", "test@example.com")
                .param("password", "abc")
                .param("confirmPassword", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("registrationDto", "password"));
    }

    @Test
    void registerWithMismatchedPasswordsShouldShowErrors() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("username", "validuser")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("confirmPassword", "different456"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("registrationDto", "confirmPassword"));
    }

    @Test
    void registerWithTakenUsernameShouldShowErrorMessage() throws Exception {
        when(userService.register(any(RegistrationDto.class)))
                .thenThrow(new IllegalArgumentException("Username already taken"));

        mockMvc.perform(post("/register").with(csrf())
                .param("username", "takenuser")
                .param("email", "new@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void loginPageWithErrorParamShouldShowErrorMessage() throws Exception {
        mockMvc.perform(get("/login").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }
}
