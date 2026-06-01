package com.sds2.country.repository;

import com.sds2.country.model.FunFact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunFactRepository extends JpaRepository<FunFact, Long> {
}
