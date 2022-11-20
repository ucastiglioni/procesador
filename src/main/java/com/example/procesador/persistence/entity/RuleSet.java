package com.example.procesador.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name ="RULE_SET" )
public class RuleSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idRuleSet;
    BigDecimal maxAmountPerTrx;
    Boolean internationTrxEnabled;
    BigDecimal maxSumAmountPerDay;
    Integer maxCountTrxPerDay;


}
