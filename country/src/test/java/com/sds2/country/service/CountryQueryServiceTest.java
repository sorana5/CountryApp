package com.sds2.country.service;

import com.sds2.country.model.Country;
import com.sds2.country.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryQueryServiceTest {

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryQueryService queryService;

    @Test
    void findById_returnsCountry_whenExists() {
        Country c = new Country();
        c.setId(1L);
        c.setName("France");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(c));

        Country result = queryService.findById(1L);

        assertEquals("France", result.getName());
        verify(countryRepository).findById(1L);
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(countryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> queryService.findById(99L));
    }

    @Test
    void findByFilters_returnsPagedResults() {
        Country c = new Country();
        c.setName("Japan");
        Page<Country> page = new PageImpl<>(List.of(c));
        when(countryRepository.findByFilters(any(), any(), any())).thenReturn(page);

        Page<Country> result = queryService.findByFilters("Japan", "", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Japan", result.getContent().get(0).getName());
    }

    @Test
    void findAll_returnsAllCountries() {
        Country c1 = new Country(); c1.setName("France");
        Country c2 = new Country(); c2.setName("Japan");
        when(countryRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Country> result = queryService.findAll();

        assertEquals(2, result.size());
    }
}