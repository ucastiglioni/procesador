package com.example.procesador.mapper;

import com.example.procesador.persistence.entity.Country;
import com.example.procesador.persistence.entity.Currency;
import com.example.procesador.persistence.entity.TransactionData;
import com.example.procesador.persistence.repository.CountryRepository;
import com.example.procesador.persistence.repository.CurrencyRepository;
import com.example.procesador.service.dto.TransactionDataInDTO;
import org.springframework.stereotype.Component;

@Component
public class TransactionDatatInDtoToTransactionData implements IMapper<TransactionDataInDTO, TransactionData> {



    public TransactionDatatInDtoToTransactionData( CountryRepository countryRepository, CurrencyRepository currencyRepository) {


    }

    @Override
    public TransactionData map(TransactionDataInDTO in) {

        TransactionData transactionData = new TransactionData();
        //el request y card se lo paso en el service ya lo tengo ahi
        transactionData.setMerchantCode(in.getMerchantCode());
        transactionData.setMerchantName(in.getMerchantName());
        transactionData.setAmount(in.getAmount());
        transactionData.setMTI(in.getMTI());
        transactionData.setTransactionIdentifier(in.getTransactionIdentifier());
        transactionData.setTransactionDate(in.getTransactionDate());

        return transactionData;
    }
}
