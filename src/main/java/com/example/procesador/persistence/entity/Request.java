package com.example.procesador.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "REQUEST")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idRequest;
    Date messageDate;
    String message;
    boolean processed;

}
