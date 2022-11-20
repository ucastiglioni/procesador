package com.example.procesador.response;

import lombok.Data;
//https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
@Data
public class ResponseCode {
    String responseCode;
    String autorizationCode;
    String data;
}
