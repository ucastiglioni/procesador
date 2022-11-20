package com.example.procesador.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class ProcesadorException extends RuntimeException {
    String message;
    HttpStatus httpStatus;

    public ProcesadorException(String message , HttpStatus httpStatus) {
        super(message);
        this.message=message;
        this.httpStatus=httpStatus;

    }
}

