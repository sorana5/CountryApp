package com.sds2.country.controller;

import com.sds2.country.client.UserServiceClient;
import com.sds2.country.config.JwtUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserServiceClient userServiceClient;
    private final JwtUtil jwtUtil;

    public AuthRestController(UserServiceClient userServiceClient, JwtUtil jwtUtil) {
        this.userServiceClient = userServiceClient;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        String storedPassword = userServiceClient.getUserPassword(username);
        String role = userServiceClient.getUserRole(username);

        if (storedPassword == null) {
            return ResponseEntity.status(401)
                    .body(new ErrorResponse("INVALID_CREDENTIALS",
                            "Invalid username or password"));
        }

        // BCrypt check via user-service
        Boolean matches = userServiceClient.checkPassword(username, password);
        if (!Boolean.TRUE.equals(matches)) {
            return ResponseEntity.status(401)
                    .body(new ErrorResponse("INVALID_CREDENTIALS",
                            "Invalid username or password"));
        }

        String token = jwtUtil.generateToken(username, role);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", username,
                "role", role));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            userServiceClient.register(
                    body.get("username"),
                    body.get("email"),
                    body.get("password"));
            return ResponseEntity.ok(Map.of("message", "Registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("REGISTRATION_FAILED", e.getMessage()));
        }
    }
}