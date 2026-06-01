package com.sds2.country.command;

import com.sds2.country.client.NotificationClient;
import com.sds2.country.client.UserServiceClient;
import com.sds2.country.model.Country;
import com.sds2.country.repository.CountryRepository;

public class CreateCountryCommand implements CountryCommand {

    private final Country country;
    private final String username;
    private final CountryRepository countryRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationClient notificationClient;

    public CreateCountryCommand(Country country, String username,
                                CountryRepository countryRepository,
                                UserServiceClient userServiceClient,
                                NotificationClient notificationClient) {
        this.country = country;
        this.username = username;
        this.countryRepository = countryRepository;
        this.userServiceClient = userServiceClient;
        this.notificationClient = notificationClient;
    }

    @Override
    public void execute() {
        countryRepository.save(country);
        String email = userServiceClient.getUserEmail(username);
        notificationClient.notifyCountryEvent(email, "CREATED", country.getName());
    }
}