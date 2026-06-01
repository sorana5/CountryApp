package com.sds2.country.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private static final String BASE = "http://localhost:8081/internal";

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUserEmail(String username) {
        try {
            return restTemplate.getForObject(
                    BASE + "/users/{username}/email",
                    String.class, username);
        } catch (Exception e) {
            System.err.println("Could not fetch email for " + username);
            return null;
        }
    }

    public boolean usernameExists(String username) {
        try {
            Boolean result = restTemplate.getForObject(
                    BASE + "/users/{username}/exists",
                    Boolean.class, username);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserRole(String username) {
        try {
            return restTemplate.getForObject(
                    BASE + "/users/{username}/role",
                    String.class, username);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserPassword(String username) {
        try {
            return restTemplate.getForObject(
                    BASE + "/users/{username}/password",
                    String.class, username);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean checkPassword(String username, String rawPassword) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(
                    Map.of("username", username, "password", rawPassword), headers);
            return restTemplate.postForObject(
                    BASE + "/users/check-password", request, Boolean.class);
        } catch (Exception e) { return false; }
    }

    public Boolean hasVisited(String username, Long countryId) {
        try {
            return restTemplate.getForObject(
                    BASE + "/users/{username}/visited/{countryId}",
                    Boolean.class, username, countryId);
        } catch (Exception e) { return false; }
    }

    public List<?> getVisited(String username) {
        try {
            return restTemplate.getForObject(
                    BASE + "/users/{username}/visited", List.class, username);
        } catch (Exception e) { return List.of(); }
    }

    public void markVisited(String username, Long countryId, String countryName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(
                    Map.of("username", username,
                            "countryId", String.valueOf(countryId),
                            "countryName", countryName), headers);
            restTemplate.postForObject(BASE + "/users/visited", request, Void.class);
        } catch (Exception e) {
            System.err.println("Could not mark visited: " + e.getMessage());
        }
    }

    public void register(String username, String email, String password) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(
                    Map.of("username", username, "email", email, "password", password),
                    headers);
            restTemplate.postForObject(
                    BASE + "/users/register", request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
}