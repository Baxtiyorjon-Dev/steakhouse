package com.steakhouse.repository;

import com.steakhouse.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxRepository extends JpaRepository<Tax, Long> {
    Optional<Tax> findByRegion(String region);
}
