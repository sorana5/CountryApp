package com.sds2.user.service;

import com.sds2.user.model.Role;
import com.sds2.user.model.User;
import com.sds2.user.model.VisitedCountry;
import com.sds2.user.repository.UserRepository;
import com.sds2.user.repository.VisitedCountryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VisitedCountryRepository visitedCountryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       VisitedCountryRepository visitedCountryRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.visitedCountryRepository = visitedCountryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String email, String password) {
        if (userRepository.existsByUsername(username))
            throw new RuntimeException("Username already taken");
        if (userRepository.existsByEmail(email))
            throw new RuntimeException("Email already registered");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public void markVisited(String username, Long countryId, String countryName) {
        if (visitedCountryRepository.existsByUserUsernameAndCountryId(username, countryId))
            return;
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        VisitedCountry v = new VisitedCountry();
        v.setUser(user);
        v.setCountryId(countryId);
        v.setCountryName(countryName);
        v.setVisitedAt(LocalDate.now());
        visitedCountryRepository.save(v);
    }

    public List<VisitedCountry> getVisited(String username) {
        return visitedCountryRepository.findByUserUsername(username);
    }
}