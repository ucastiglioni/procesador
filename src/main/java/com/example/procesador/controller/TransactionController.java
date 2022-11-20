package com.example.procesador.controller;

import com.example.procesador.persistence.entity.ResponseCode;
import com.example.procesador.persistence.entity.TransactionData;
import com.example.procesador.service.TransactionService;
import com.example.procesador.service.dto.TransaccionProcesadaDTO;
import com.google.gson.*;
import net.bytebuddy.description.type.TypeVariableToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.util.List;

@RequestMapping("/transactions")
@RestController
public class TransactionController {

    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    /*@GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<EntityDto> getAll() {
        return entityService.getAllEntities();
    }*/

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,path = "/transactions/{dateString}")
    @ResponseStatus(HttpStatus.OK)
    public String findProcessedTransactionDataByDate(@PathVariable ("dateString")String dateString ) {
        String s=this.transactionService.getTransactionDataByTransactionDate(dateString);
        return s;
//        String responseJson2 = "[{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":1,\"cantidad\":7},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":8,\"cantidad\":8},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":9,\"cantidad\":3},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":10,\"cantidad\":1},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":11,\"cantidad\":1},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":8,\"cantidad\":1},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":9,\"cantidad\":3},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":10,\"cantidad\":1},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":11,\"cantidad\":3}]";
//        Date date = Date.valueOf(dateString);
//        String responseJson = this.transactionService.getTransactionDataByTransactionDate(date);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String json = gson.toJson(responseJson);
//
//
//        TransaccionProcesadaDTO[] transaccionesProcesadas = gson.fromJson(responseJson2,TransaccionProcesadaDTO[].class);
//
//        for (TransaccionProcesadaDTO trProcesada : transaccionesProcesadas) {
//            System.out.println(trProcesada);
//        }
//
//        //-------------------------------
//        return json;
////        JsonObject obj = new JsonParser().parse(responseJson2).getAsJsonObject();
////         return obj.toString();
    }

}
