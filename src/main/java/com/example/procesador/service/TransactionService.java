package com.example.procesador.service;

import com.example.procesador.exception.ProcesadorException;
import com.example.procesador.mapper.RequestInDtoToRequest;
import com.example.procesador.mapper.TransactionDatatInDtoToTransactionData;
import com.example.procesador.persistence.entity.*;
import com.example.procesador.persistence.repository.*;
import com.example.procesador.service.dto.RequestInDTO;
import com.example.procesador.service.dto.TransaccionProcesadaDTO;
import com.example.procesador.service.dto.TransactionDataInDTO;
import com.example.procesador.util.LogControl;
import com.example.procesador.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class TransactionService {


   private final TransactionDataRepository transactionDataRepository;

    private final TransactionDatatInDtoToTransactionData mapperTransactionData;




    public TransactionService(TransactionDataRepository transactionDataRepository, TransactionDatatInDtoToTransactionData mapperTransactionData) {

        this.transactionDataRepository = transactionDataRepository;
        this.mapperTransactionData = mapperTransactionData;

    }

    public String getTransactionDataByTransactionDate(String dateString) {
        String responseJson2 = "[{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":1,\"cantidad\":7},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":8,\"cantidad\":8},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":9,\"cantidad\":3},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":10,\"cantidad\":1},{\"ID_COUNTRY\":1,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":11,\"cantidad\":1},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":8,\"cantidad\":1},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":9,\"cantidad\":3},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":10,\"cantidad\":1},{\"ID_COUNTRY\":2,\"MERCHANT_NAME\":\"EVERTECURUGUAY                                              \",\"code\":11,\"cantidad\":3}]";
        Date date = Date.valueOf(dateString);
        String responseJson = this.transactionDataRepository.getTransactionDataByTransactionDate(date);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(responseJson);


        TransaccionProcesadaDTO[] transaccionesProcesadas = gson.fromJson(responseJson2,TransaccionProcesadaDTO[].class);

        for (TransaccionProcesadaDTO trProcesada : transaccionesProcesadas) {
            System.out.println(trProcesada);
        }

        //-------------------------------
        return json;

    }

}
