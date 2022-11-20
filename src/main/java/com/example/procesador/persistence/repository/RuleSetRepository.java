package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.Card;
import com.example.procesador.persistence.entity.Request;
import com.example.procesador.persistence.entity.RuleSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RuleSetRepository extends JpaRepository<RuleSet, Integer> {


}
