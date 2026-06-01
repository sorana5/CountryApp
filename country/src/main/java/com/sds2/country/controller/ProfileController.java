package com.sds2.country.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private final RestTemplate restTemplate;

    public ProfileController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(
                    "http://localhost:8081/internal/users/{username}/visited",
                    List.class, principal.getName());
            model.addAttribute("visited", response.getBody());
        } catch (Exception e) {
            model.addAttribute("visited", List.of());
        }
        return "profile";
    }

    @PostMapping("/countries/{id}/visit")
    public String markVisited(@PathVariable Long id,
                              @RequestParam String countryName,
                              Principal principal) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(
                    Map.of("username", principal.getName(),
                            "countryId", String.valueOf(id),
                            "countryName", countryName),
                    headers);
            restTemplate.postForObject(
                    "http://localhost:8081/internal/users/visited",
                    request, Void.class);
        } catch (Exception e) {
            System.err.println("Could not mark visited: " + e.getMessage());
        }
        return "redirect:/countries/" + id;
    }
}