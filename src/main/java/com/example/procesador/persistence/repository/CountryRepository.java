package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.Card;
import com.example.procesador.persistence.entity.Country;
import com.example.procesador.persistence.entity.ResponseCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query(value="SELECT * FROM COUNTRY WHERE COUNTRY_CODE = ?1", nativeQuery = true)
    Country findByCountryCode(String countryCode);

}
