package com.games.final_projest_java.service;

import com.games.final_projest_java.dto.RegistrationDto;
import com.games.final_projest_java.model.User;
import com.games.final_projest_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    public User register(RegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void updateTTTStats(String username, String result) {
        User user = findByUsername(username);
        switch (result) {
            case "WIN"  -> user.setTttWins(user.getTttWins() + 1);
            case "LOSS" -> user.setTttLosses(user.getTttLosses() + 1);
            case "DRAW" -> user.setTttDraws(user.getTttDraws() + 1);
        }
        userRepository.save(user);
    }

    public void updateRPSStats(String username, String result) {
        User user = findByUsername(username);
        switch (result) {
            case "WIN"  -> user.setRpsWins(user.getRpsWins() + 1);
            case "LOSS" -> user.setRpsLosses(user.getRpsLosses() + 1);
            case "DRAW" -> user.setRpsDraws(user.getRpsDraws() + 1);
        }
        userRepository.save(user);
    }
}
