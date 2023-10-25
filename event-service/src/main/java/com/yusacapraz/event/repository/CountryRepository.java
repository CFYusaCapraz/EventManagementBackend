package com.yusacapraz.event.repository;

import com.yusacapraz.event.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findCountryByCountryName(String countryName);
}
