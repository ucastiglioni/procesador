package com.example.procesador.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
//import java.util.Date;

@Data
@Entity
@Table(name = "TRANSACTION_DATA")
public class TransactionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idTransaction;
    int MTI;
    @ManyToOne
    @JoinColumn(name="id_card")
    Card card;
    Date transactionDate;
    BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "id_currency")
    Currency currency;
    String merchantCode;
    String merchantName;
    @ManyToOne
    @JoinColumn (name = "id_country")
    Country country;
    @OneToOne
    @JoinColumn(name="id_request")
    Request request;
    String transactionIdentifier;
    @OneToOne
    @JoinColumn(name="id_response_code")
    ResponseCode responseCode;
    @OneToOne
    @JoinColumn(name="id_rejection_rule_set")
    RuleSet rejectionRuleSet;
    int authorizationCode;
}
