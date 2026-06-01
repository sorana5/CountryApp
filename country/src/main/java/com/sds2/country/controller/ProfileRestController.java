package com.sds2.country.controller;

import com.sds2.country.client.UserServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {

    private final UserServiceClient userServiceClient;

    public ProfileRestController(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("/visited")
    public ResponseEntity<?> getVisited(Principal principal) {
        var visited = userServiceClient.getVisited(principal.getName());
        return ResponseEntity.ok(visited);
    }

    @PostMapping("/visited/{countryId}")
    public ResponseEntity<?> markVisited(@PathVariable Long countryId,
                                         @RequestBody Map<String, String> body,
                                         Principal principal) {
        userServiceClient.markVisited(
                principal.getName(), countryId, body.get("countryName"));
        return ResponseEntity.ok(Map.of("message", "Marked as visited"));
    }

    @GetMapping("/visited/{countryId}/check")
    public ResponseEntity<?> checkVisited(@PathVariable Long countryId,
                                          Principal principal) {
        Boolean visited = userServiceClient.hasVisited(
                principal.getName(), countryId);
        return ResponseEntity.ok(Map.of("visited", Boolean.TRUE.equals(visited)));
    }
}