package com.sds2.country.service;

import com.sds2.country.client.NotificationClient;
import com.sds2.country.client.UserServiceClient;
import com.sds2.country.command.*;
import com.sds2.country.model.Country;
import com.sds2.country.model.FunFact;
import com.sds2.country.repository.CountryRepository;
import org.springframework.stereotype.Service;

@Service
public class CountryCommandService {

    private final CountryRepository countryRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationClient notificationClient;

    public CountryCommandService(CountryRepository countryRepository,
                                 UserServiceClient userServiceClient,
                                 NotificationClient notificationClient) {
        this.countryRepository = countryRepository;
        this.userServiceClient = userServiceClient;
        this.notificationClient = notificationClient;
    }

    public void create(Country country, String username) {
        new CreateCountryCommand(country, username,
                countryRepository, userServiceClient, notificationClient).execute();
    }

    public void update(Long id, Country country, String username) {
        new UpdateCountryCommand(id, country, username,
                countryRepository, userServiceClient, notificationClient).execute();
    }

    public void delete(Long id, String username) {
        new DeleteCountryCommand(id, username,
                countryRepository, userServiceClient, notificationClient).execute();
    }

    public void addFunFact(Long countryId, String content, String username) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        FunFact fact = new FunFact();
        fact.setContent(content);
        fact.setCountry(country);
        fact.setAuthorUsername(username);
        country.getFunFacts().add(fact);
        countryRepository.save(country);
    }

    public void deleteFunFact(Long countryId, Long factId, String adminUsername) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        country.getFunFacts().stream()
                .filter(f -> f.getId().equals(factId))
                .findFirst()
                .ifPresent(fact -> {
                    String email = userServiceClient.getUserEmail(fact.getAuthorUsername());
                    notificationClient.notifyFunFactDeleted(email, fact.getContent(), country.getName());
                });

        country.getFunFacts().removeIf(f -> f.getId().equals(factId));
        countryRepository.save(country);
    }
}