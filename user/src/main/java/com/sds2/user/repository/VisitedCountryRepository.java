package com.sds2.user.repository;

import com.sds2.user.model.VisitedCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitedCountryRepository extends JpaRepository<VisitedCountry, Long> {
    List<VisitedCountry> findByUserUsername(String username);
    boolean existsByUserUsernameAndCountryId(String username, Long countryId);
}