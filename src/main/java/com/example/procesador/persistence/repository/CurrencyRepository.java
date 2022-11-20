package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.Card;
import com.example.procesador.persistence.entity.Country;
import com.example.procesador.persistence.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    @Query(value="SELECT * FROM CURRENCY WHERE CURRENCY_CODE = ?1", nativeQuery = true)
    Currency findByCurrencyCode(String currencyCode);
}
