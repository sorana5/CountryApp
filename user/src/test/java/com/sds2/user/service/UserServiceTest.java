package com.sds2.user.service;

import com.sds2.user.model.Role;
import com.sds2.user.model.User;
import com.sds2.user.model.VisitedCountry;
import com.sds2.user.repository.UserRepository;
import com.sds2.user.repository.VisitedCountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock VisitedCountryRepository visitedCountryRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void register_savesUserWithEncodedPassword() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");

        userService.register("alice", "alice@test.com", "password123");

        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("alice") &&
                        u.getPassword().equals("hashed") &&
                        u.getRole() == Role.USER));
    }

    @Test
    void register_throwsException_whenUsernameTaken() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> userService.register("alice", "alice@test.com", "password123"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_throwsException_whenEmailTaken() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> userService.register("alice", "alice@test.com", "password123"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void markVisited_savesEntry_whenNotAlreadyVisited() {
        User user = new User();
        user.setUsername("alice");
        when(visitedCountryRepository
                .existsByUserUsernameAndCountryId("alice", 1L)).thenReturn(false);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        userService.markVisited("alice", 1L, "France");

        verify(visitedCountryRepository).save(argThat(v ->
                v.getCountryName().equals("France") &&
                        v.getCountryId().equals(1L)));
    }

    @Test
    void markVisited_doesNothing_whenAlreadyVisited() {
        when(visitedCountryRepository
                .existsByUserUsernameAndCountryId("alice", 1L)).thenReturn(true);

        userService.markVisited("alice", 1L, "France");

        verify(visitedCountryRepository, never()).save(any());
    }

    @Test
    void getVisited_returnsUserVisitedList() {
        VisitedCountry v = new VisitedCountry();
        v.setCountryName("Japan");
        when(visitedCountryRepository.findByUserUsername("alice"))
                .thenReturn(List.of(v));

        List<VisitedCountry> result = userService.getVisited("alice");

        assertEquals(1, result.size());
        assertEquals("Japan", result.get(0).getCountryName());
    }
}