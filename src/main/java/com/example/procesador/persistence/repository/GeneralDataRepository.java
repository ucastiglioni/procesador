package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.Country;
import com.example.procesador.persistence.entity.GeneralData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GeneralDataRepository extends JpaRepository<GeneralData, Integer> {

    @Query(value="SELECT TOP 1 * FROM GENERAL_DATA", nativeQuery = true)
    GeneralData findTop1();

}
