package com.example.procesador.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name ="GENERAL_DATA" )
public class GeneralData {
    @Id
    Integer idCountry;
    Date businessDate;

}