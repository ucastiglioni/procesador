package com.example.procesador.util;

import com.example.procesador.exception.ProcesadorException;
import com.example.procesador.service.dto.RequestInDTO;
import com.example.procesador.service.dto.TransactionDataInDTO;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class Validator {
    public static boolean validateJavaDate(String strDate)
    {
        /* Check if date is 'null' */
        if (strDate.trim().equals(""))
        {
            return true;
        }
        /* Date is not 'null' */
        else
        {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyyMMdd");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try
            {
                java.util.Date javaDate = sdfrmt.parse(strDate);
                System.out.println(strDate+" is valid date format");
            }
            /* Date format is invalid */
            catch (ParseException e)
                /* Return true if date format is valid */
            {
                System.out.println(strDate+" is Invalid Date format");
                return false;
            }
            return true;
        }
    }


    public static boolean validarMTI(String substring) {

        //MTI
        //4
        //Tipo de mensaje
        //Es un MTI válido según la ISO-8583
        //(https://es.wikipedia.org/wiki/ISO_8583)

        //se uso esta libreria para validar Isos
        //https://github.com/TakahikoKawasaki/nv-i18n
        for (CountryCode code : CountryCode.values()){

            if((code.getNumeric() == Integer.parseInt(substring))){
                return true;
            }
        }
        Logger.getLogger("No se validó MTI code:  "+substring);
        return false;
    }

    public static boolean validarCodigoMoneda(String substring) {
        //todomaxi
        // Currency
        //3
        //Código de la moneda de la cuenta – Debe ser un código
        //numérico iso3 de moneda de la ISO-4217 (https://es.wikipedia.org/wiki/ISO_4217)
        //Ej: “858” para Pesos Uruguayos

        for (CurrencyCode code : CurrencyCode.values()){
            if((code.getNumeric() == Integer.parseInt(substring))){
                Logger.getLogger("Se validó Currency code:  "+substring);
                return true;
            }
        }
        Logger.getLogger("No se validó currency code:  "+substring);
        return false;
    }

    public static boolean validarCodigoPais(String substring) {
        //todomaxi
        //MerchanCountryCode
        //3
        //Código numérico del país en la ISO-3166
        //(https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes)
        //Ej. “858” Uruguay
        for (CountryCode code : CountryCode.values()){
            if((code.getNumeric() == Integer.parseInt(substring))){
                Logger.getLogger("Se validó Country code:  "+substring);
                return true;
            }
        }
        Logger.getLogger("No se validó Country code:  "+substring);
        return false;

    }



//    public static boolean dameMonto(String monto) {
//        //todomaxi
//        //manejo de exception, guardar log
//        try {
//            //String str = message.substring(44, 56);
//            monto = new StringBuilder(monto).insert(monto.length() - 3, ".").toString();
//            BigDecimal bd = new BigDecimal(monto);
//            System.out.println("dameMonto: "+bd);
//        }catch(Exception ex){
//            throw new TodoExceptions("Error al procesar request: monto inválido", HttpStatus.NOT_FOUND);
//        }
//        return true;
//    }

    //Este metodo valida el formato y devuelve un TransactionDataDTO valido para persistir
    public static TransactionDataInDTO getTransactionData(RequestInDTO requestInDto) throws ProcesadorException {

        String message=requestInDto.getMessage();

        //valido largo 132
        if (message.length()!=132){
            throw new ProcesadorException("largo inválido",HttpStatus.BAD_REQUEST);
        }

        //valido separadores //
        TransactionDataInDTO transactionDataInDTO=new TransactionDataInDTO();
        transactionDataInDTO.setCardNumber(message.substring(0,16));
        //valido separador 1
        Character ch1=message.charAt(16);
        Character separador1="%".charAt(0);
        if(Character.compare(ch1,separador1)!=0){
            throw new ProcesadorException("Procesador Inválido", HttpStatus.BAD_REQUEST);
        }

        //Valido fecha
        try {
            String str = message.substring(17, 25);
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            Date sqlDate;
            sqlDate=new java.sql.Date(df.parse(str).getTime());
            transactionDataInDTO.setTransactionDate(sqlDate);
        }catch (Exception ex){
            throw new ProcesadorException("Fecha inválida", HttpStatus.BAD_REQUEST);
        }

        // Valido TransactionIdentifier
        try{
            transactionDataInDTO.setTransactionIdentifier(message.substring(25,37));
        }catch (Exception ex){
            throw new ProcesadorException("TransactionIdentifier inválido", HttpStatus.BAD_REQUEST);
        }

        //valido separador 2
        Character ch2=message.charAt(16);
        Character separador2="%".charAt(0);
        if(Character.compare(ch2,separador2)!=0){
            throw new ProcesadorException("Procesador2 Inválido", HttpStatus.BAD_REQUEST);
        }

        //Valido MTI
        try {
            String sMTI=message.substring(38, 42);
            validarMTI(sMTI);
            transactionDataInDTO.setMTI(Integer.parseInt(sMTI));

        }catch(Exception ex){
            throw new ProcesadorException("MTI Inválido", HttpStatus.BAD_REQUEST);
        }

        //Valido Currency
        try{
            transactionDataInDTO.setCurrencyCode(Integer.parseInt(message.substring(42,45)));
        }catch (Exception ex){
            throw new ProcesadorException("Currency inválido", HttpStatus.BAD_REQUEST);
        }

        //Valido Amount
        try {
            transactionDataInDTO.setAmount(BigDecimal.valueOf(Long.parseLong(message.substring(45,57))));
        }catch(Exception e){
            throw new ProcesadorException("Amount inválido", HttpStatus.BAD_REQUEST);
        }

        //Valido MerchantCode
        try {
            transactionDataInDTO.setMerchantCode(message.substring(57,69));
        }catch (Exception e){
            throw new ProcesadorException("MerchantCode invalido", HttpStatus.BAD_REQUEST);
        }

        //Valido MerchantName
        try {
            transactionDataInDTO.setMerchantName(message.substring(69,129));
        }catch (Exception e){
            throw new ProcesadorException("MerchantName invalido", HttpStatus.BAD_REQUEST);
        }

        //Valido MerchantCountryCode
        try {
            String codPais=message.substring(129,132);
            if(validarCodigoPais(codPais)){
                transactionDataInDTO.setMerchantCountryCode(Integer.parseInt(codPais));
            }else{
                throw new ProcesadorException("MerchantCountryCode invalido", HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            throw new ProcesadorException("MerchantCountryCode invalido", HttpStatus.BAD_REQUEST);
        }
        return transactionDataInDTO;
    }
}
