package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {

}
