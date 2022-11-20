package com.example.procesador.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="CURRENCY")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idCurrency;
    int currencyCode;
    String currencyName;

}
