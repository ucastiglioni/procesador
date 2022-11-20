package com.example.procesador.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

//import java.util.Date;
@Data
public class TransactionDataInDTO {


    int MTI;
    String cardNumber;
    Date transactionDate;
    BigDecimal amount;
    int currencyCode;
    String merchantCode;
    String merchantName;
    int merchantCountryCode;
    String transactionIdentifier;
    int idRejectionRuleSet;


}
