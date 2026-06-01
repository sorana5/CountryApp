package com.sds2.country.service;

import com.sds2.country.model.Country;
import com.sds2.country.repository.CountryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CountryQueryService {

    private final CountryRepository countryRepository;

    public CountryQueryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country findById(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    public Page<Country> findByFilters(String name, String continent, Pageable pageable) {
        return countryRepository.findByFilters(
                name == null ? "" : name.trim(),
                continent == null ? "" : continent.trim(),
                pageable);
    }

    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}