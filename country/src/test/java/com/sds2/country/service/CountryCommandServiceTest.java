package com.sds2.country.service;

import com.sds2.country.client.NotificationClient;
import com.sds2.country.client.UserServiceClient;
import com.sds2.country.model.Country;
import com.sds2.country.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryCommandServiceTest {

    @Mock CountryRepository countryRepository;
    @Mock UserServiceClient userServiceClient;
    @Mock NotificationClient notificationClient;

    @InjectMocks
    CountryCommandService commandService;

    @Test
    void create_savesCountryAndSendsNotification() {
        Country c = new Country();
        c.setName("Germany");
        when(userServiceClient.getUserEmail("admin")).thenReturn("admin@test.com");

        commandService.create(c, "admin");

        verify(countryRepository).save(c);
        verify(notificationClient).notifyCountryEvent("admin@test.com", "CREATED", "Germany");
    }

    @Test
    void delete_removesCountryAndSendsNotification() {
        Country c = new Country();
        c.setId(1L);
        c.setName("France");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(c));
        when(userServiceClient.getUserEmail("admin")).thenReturn("admin@test.com");

        commandService.delete(1L, "admin");

        verify(countryRepository).deleteById(1L);
        verify(notificationClient).notifyCountryEvent("admin@test.com", "DELETED", "France");
    }

    @Test
    void update_savesWithCorrectId() {
        Country c = new Country();
        c.setName("Updated France");
        when(userServiceClient.getUserEmail("admin")).thenReturn("admin@test.com");

        commandService.update(1L, c, "admin");

        assertEquals(1L, c.getId());
        verify(countryRepository).save(c);
    }
}