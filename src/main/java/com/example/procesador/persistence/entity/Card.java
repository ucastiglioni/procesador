package com.example.procesador.persistence.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
@Data
@Entity
@Table (name = "CARD")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idCard;
    String cardNumber;
    boolean lockedOrCanceled;
    Date dueDate;
    BigDecimal balance;
    @OneToOne
    @JoinColumn(name="id_rule_set")
    RuleSet ruleSet;

    @ManyToOne
    @JoinColumn(name = "id_currency")
    Currency currency;

}
