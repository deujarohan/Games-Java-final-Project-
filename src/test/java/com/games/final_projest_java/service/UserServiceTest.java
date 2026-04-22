package com.games.final_projest_java.service;

import com.games.final_projest_java.dto.RegistrationDto;
import com.games.final_projest_java.model.User;
import com.games.final_projest_java.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegistrationDto dto;

    @BeforeEach
    void setUp() {
        dto = new RegistrationDto();
        dto.setUsername("newplayer");
        dto.setEmail("new@example.com");
        dto.setPassword("secret123");
        dto.setConfirmPassword("secret123");
    }

    @Test
    void registerShouldSaveNewUser() {
        when(userRepository.existsByUsername("newplayer")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User user = userService.register(dto);

        assertThat(user.getUsername()).isEqualTo("newplayer");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getPassword()).isEqualTo("hashed");
        assertThat(user.getRole()).isEqualTo("ROLE_USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerShouldThrowWhenUsernameTaken() {
        when(userRepository.existsByUsername("newplayer")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already taken");

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerShouldThrowWhenEmailTaken() {
        when(userRepository.existsByUsername("newplayer")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already registered");

        verify(userRepository, never()).save(any());
    }

    @Test
    void findByUsernameShouldReturnUser() {
        User user = User.builder().username("alice").build();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("alice");

        assertThat(result.getUsername()).isEqualTo("alice");
    }

    @Test
    void findByUsernameShouldThrowWhenNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void updateTTTStatsShouldIncrementWins() {
        User user = User.builder().username("bob").tttWins(2).build();
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.updateTTTStats("bob", "WIN");

        assertThat(user.getTttWins()).isEqualTo(3);
        verify(userRepository).save(user);
    }

    @Test
    void updateTTTStatsShouldIncrementLosses() {
        User user = User.builder().username("bob").tttLosses(1).build();
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.updateTTTStats("bob", "LOSS");

        assertThat(user.getTttLosses()).isEqualTo(2);
    }

    @Test
    void updateTTTStatsShouldIncrementDraws() {
        User user = User.builder().username("bob").tttDraws(0).build();
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.updateTTTStats("bob", "DRAW");

        assertThat(user.getTttDraws()).isEqualTo(1);
    }

    @Test
    void updateRPSStatsShouldIncrementWins() {
        User user = User.builder().username("carol").rpsWins(5).build();
        when(userRepository.findByUsername("carol")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.updateRPSStats("carol", "WIN");

        assertThat(user.getRpsWins()).isEqualTo(6);
    }

    @Test
    void registrationDtoPasswordMatchingShouldReturnTrueWhenMatch() {
        assertThat(dto.isPasswordMatching()).isTrue();
    }

    @Test
    void registrationDtoPasswordMatchingShouldReturnFalseWhenMismatch() {
        dto.setConfirmPassword("different");
        assertThat(dto.isPasswordMatching()).isFalse();
    }
}
