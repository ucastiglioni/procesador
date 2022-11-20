package com.example.procesador.controller;

import com.example.procesador.persistence.entity.Request;
import com.example.procesador.service.RequestService;
import com.example.procesador.service.dto.RequestInDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/requests") //esta anotacion nos permite definir el camino, la ruta  de nuestro controllador
@RestController
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE) //nos va a permitir crear elementos, dar de alta
    public String createRequest(@RequestBody RequestInDTO requestInDto){
        return this.requestService.createRequest(requestInDto);
    }
}
