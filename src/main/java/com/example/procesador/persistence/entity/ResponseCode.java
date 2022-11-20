package com.example.procesador.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="RESPONSE_CODE")
public class ResponseCode {
    /*{
"responseCode": "59",
"authorizationCode": 0
" description ": "Insuficient funds"
*/
    @Id
    @Column(name = "ID_RESPONSE_CODE")
    Integer idResponseCode;
    @Column(name = "CODE")
    String authorizationCode;
    String description;
}
