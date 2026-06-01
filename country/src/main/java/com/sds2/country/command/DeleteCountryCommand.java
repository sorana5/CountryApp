package com.sds2.country.command;

import com.sds2.country.client.NotificationClient;
import com.sds2.country.client.UserServiceClient;
import com.sds2.country.repository.CountryRepository;

public class DeleteCountryCommand implements CountryCommand {

    private final Long id;
    private final String username;
    private final CountryRepository countryRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationClient notificationClient;

    public DeleteCountryCommand(Long id, String username,
                                CountryRepository countryRepository,
                                UserServiceClient userServiceClient,
                                NotificationClient notificationClient) {
        this.id = id;
        this.username = username;
        this.countryRepository = countryRepository;
        this.userServiceClient = userServiceClient;
        this.notificationClient = notificationClient;
    }

    @Override
    public void execute() {
        countryRepository.findById(id).ifPresent(country -> {
            String email = userServiceClient.getUserEmail(username);
            countryRepository.deleteById(id);
            notificationClient.notifyCountryEvent(email, "DELETED", country.getName());
        });
    }
}