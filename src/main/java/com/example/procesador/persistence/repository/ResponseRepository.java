package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.Card;
import com.example.procesador.persistence.entity.ResponseCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResponseRepository extends JpaRepository<ResponseCode, Integer> {

    @Query(value="SELECT * FROM RESPONSE_CODE WHERE CODE= ?1", nativeQuery = true)
    ResponseCode findByCode(String code);

}
