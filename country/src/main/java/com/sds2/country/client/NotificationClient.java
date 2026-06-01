package com.sds2.country.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate;
    private static final String BASE = "http://localhost:8082/notify";

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyCountryEvent(String email, String type, String countryName) {
        if (email == null) return;
        try {
            post(BASE + "/country",
                    Map.of("email", email, "type", type, "countryName", countryName));
        } catch (Exception e) {
            System.err.println("Notification service unavailable: " + e.getMessage());
        }
    }

    public void notifyFunFactDeleted(String email, String content, String countryName) {
        if (email == null) return;
        try {
            post(BASE + "/funfact-deleted",
                    Map.of("email", email, "content", content, "countryName", countryName));
        } catch (Exception e) {
            System.err.println("Notification service unavailable: " + e.getMessage());
        }
    }

    private void post(String url, Map<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(url, request, Void.class);
    }
//    private final RestTemplate restTemplate;
//    private static final String BASE = "http://localhost:8082/notify";
//
//    public NotificationClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public void notifyCountryEvent(String email, String type, String countryName) {
//        if (email == null) return;
//        try {
//            restTemplate.postForObject(BASE + "/country",
//                    Map.of("email", email, "type", type, "countryName", countryName),
//                    Void.class);
//        } catch (Exception e) {
//            System.err.println("Notification service unavailable: " + e.getMessage());
//        }
//    }
//
//    public void notifyFunFactDeleted(String email, String content, String countryName) {
//        if (email == null) return;
//        try {
//            restTemplate.postForObject(BASE + "/funfact-deleted",
//                    Map.of("email", email, "content", content, "countryName", countryName),
//                    Void.class);
//        } catch (Exception e) {
//            System.err.println("Notification service unavailable: " + e.getMessage());
//        }
//    }
}