package com.sds2.country.repository;

import com.sds2.country.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT c FROM Country c WHERE " +
            "(:name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:continent = '' OR LOWER(c.continent) = LOWER(:continent))")
    Page<Country> findByFilters(@Param("name") String name,
                                @Param("continent") String continent,
                                Pageable pageable);
}