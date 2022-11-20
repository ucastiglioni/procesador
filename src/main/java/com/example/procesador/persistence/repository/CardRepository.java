package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.Card;
import com.example.procesador.persistence.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    @Query(value="SELECT * FROM CARD WHERE CARD_NUMBER = ?1", nativeQuery = true)
    Card findByCardNumber(String cardNumber);

}
