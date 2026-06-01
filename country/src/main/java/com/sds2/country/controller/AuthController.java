package com.sds2.country.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class AuthController {

    private final RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(
                    Map.of("username", username, "email", email, "password", password),
                    headers
            );
            restTemplate.postForObject(
                    "http://localhost:8081/internal/users/register",
                    request, Void.class);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
//        try {
//            restTemplate.postForObject(
//                    "http://localhost:8081/internal/users/register",
//                    Map.of("username", username, "email", email, "password", password),
//                    Void.class);
//            return "redirect:/login";
//        } catch (Exception e) {
//            model.addAttribute("error", "Registration failed");
//            return "register";
//        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
