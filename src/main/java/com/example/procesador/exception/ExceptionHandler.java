package com.example.procesador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@RestController
public class ExceptionHandler {
    private class JSonResponse{
        String message;
        public JSonResponse(){

        }
        public JSonResponse(String message){
            super();
            this.message=message;
        }
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ResponseEntity<JSonResponse> handleException(ProcesadorException e) {
        return new ResponseEntity<JSonResponse>(new JSonResponse(e.getMessage()), e.getHttpStatus());
    }
}
