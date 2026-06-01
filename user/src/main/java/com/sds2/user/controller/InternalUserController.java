package com.sds2.user.controller;

import com.sds2.user.model.VisitedCountry;
import com.sds2.user.repository.VisitedCountryRepository;
import com.sds2.user.service.UserService;
import com.sds2.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal")
public class InternalUserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final VisitedCountryRepository visitedCountryRepository;
    private final PasswordEncoder passwordEncoder;

    public InternalUserController(UserRepository userRepository,
                                  UserService userService, VisitedCountryRepository visitedCountryRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.visitedCountryRepository = visitedCountryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/{username}/email")
    public ResponseEntity<String> getEmail(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(u -> ResponseEntity.ok(u.getEmail()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{username}/password")
    public ResponseEntity<String> getPassword(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(u -> ResponseEntity.ok(u.getPassword()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{username}/role")
    public ResponseEntity<String> getRole(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(u -> ResponseEntity.ok(u.getRole().name()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{username}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable String username) {
        return ResponseEntity.ok(userRepository.existsByUsername(username));
    }

    @PostMapping("/users/register")
    public ResponseEntity<Void> register(@RequestBody Map<String, String> body) {
        try {
            userService.register(
                    body.get("username"),
                    body.get("email"),
                    body.get("password"));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/users/visited")
    public ResponseEntity<Void> markVisited(@RequestBody Map<String, String> body) {
        userService.markVisited(
                body.get("username"),
                Long.parseLong(body.get("countryId")),
                body.get("countryName"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{username}/visited")
    public ResponseEntity<List<VisitedCountry>> getVisited(@PathVariable String username) {
        return ResponseEntity.ok(userService.getVisited(username));
    }

    @GetMapping("/users/{username}/visited/{countryId}")
    public ResponseEntity<Boolean> hasVisited(@PathVariable String username,
                                              @PathVariable Long countryId) {
        return ResponseEntity.ok(
                visitedCountryRepository.existsByUserUsernameAndCountryId(username, countryId));
    }

    @PostMapping("/users/check-password")
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, String> body) {
        return userRepository.findByUsername(body.get("username"))
                .map(u -> ResponseEntity.ok(
                        passwordEncoder.matches(body.get("password"), u.getPassword())))
                .orElse(ResponseEntity.ok(false));
    }
}