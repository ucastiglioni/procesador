package com.example.procesador.service;

import com.example.procesador.exception.ProcesadorException;
import com.example.procesador.mapper.RequestInDtoToRequest;
import com.example.procesador.mapper.TransactionDatatInDtoToTransactionData;
import com.example.procesador.persistence.entity.*;
import com.example.procesador.persistence.repository.*;
import com.example.procesador.service.dto.RequestInDTO;
import com.example.procesador.service.dto.TransactionDataInDTO;
import com.example.procesador.util.LogControl;
import com.example.procesador.util.Validator;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

@Service
public class RequestService {


    private final RequestRepository requestRepository;
    private final CardRepository cardRepository;
    private final RuleSetRepository ruleSetRepository;
    private final ResponseRepository responseRepository;
    private final GeneralDataRepository generalDataRepository;

    private final CountryRepository countryrepository;

    private final CurrencyRepository currencyRepository;
    private final TransactionDataRepository transactionDataRepository;
    private final RequestInDtoToRequest mapperRequest;
    private final TransactionDatatInDtoToTransactionData mapperTransactionData;




    public RequestService(RequestRepository requestRepository, CardRepository cardRepository, RuleSetRepository ruleSetRepository, ResponseRepository responseRepository, GeneralDataRepository generalDataRepository, CountryRepository countryrepository, CurrencyRepository currencyRepository, TransactionDataRepository transactionDataRepository, RequestInDtoToRequest mapperRequest, TransactionDatatInDtoToTransactionData mapperTransactionData) {
        this.requestRepository = requestRepository;
        this.cardRepository = cardRepository;
        this.ruleSetRepository = ruleSetRepository;
        this.responseRepository = responseRepository;
        this.generalDataRepository = generalDataRepository;
        this.countryrepository = countryrepository;
        this.currencyRepository = currencyRepository;
        this.transactionDataRepository = transactionDataRepository;
        this.mapperRequest = mapperRequest;
        this.mapperTransactionData = mapperTransactionData;

    }


    public String createRequest(RequestInDTO requestInDto) {

        Request request = mapperRequest.map(requestInDto);
        LogControl.log("info", "********* Inicio ********");
        LogControl.log("info", "********* Request: " + requestInDto.getMessage());
        Card card=null;
        Country merchantCountry=null;
        Currency currency=null;
        TransactionDataInDTO transactionDataInDTO=null;
        RuleSet ruleSet = null;
        String responseCode="00"; //se mapea con code en la tabla y con authorizationCode en la clase responseCode
        int authorizationCode=0;

        ResponseCode trResponseCode;

        //---------------------------------------------------------//
        //------------Validación de formato------------------------//
        //
        //Valido Formato y devuelvo transactionDto
        //trataremos de armar un TransactionDataInDTO con lo que viene en el request.
        // si valida el formato del request ya me devuelve
        // un DTO TransactionDataInDTO para armar transactionData.
        //persisto el request como "procesado"
        // si viene un error de formato arroja una exception y en el
        // catch persisto el request como "no procesado"
        //---------------------------------------------------------//

        try {
            LogControl.log("info", "********* Validación de formato *********");
            transactionDataInDTO = Validator.getTransactionData(requestInDto);

            merchantCountry= countryrepository.findByCountryCode(transactionDataInDTO.getMerchantCountryCode()+"");
            currency=currencyRepository.findByCurrencyCode(transactionDataInDTO.getCurrencyCode()+"");
            if(merchantCountry==null || currency==null){
                // no paso validacionFormato
                //guardo log
                //guardo request sin procesar
                //retorno json con el error de autorizacion.
                LogControl.log("error", "********* Formato inválido: pais o moneda con formato valido pero invalido en el sistema");
                request.setProcessed(false);
                request = this.requestRepository.save(request);
                LogControl.log("warn","********* Formato invalido: Se guardo request sin procesar" );

                return responseJson("30",0,"Format error");
            }



        } catch (Exception ex) {

            // no paso validacionFormato
            //guardo log
            //guardo request sin procesar
            //retorno json con el error de autorizacion.
            LogControl.log("error", "********* Formato inválido: " + ex.getMessage());
            request.setProcessed(false);
            request = this.requestRepository.save(request);
            LogControl.log("warn","********* Formato invalido: Se guardo request sin procesar" );

            return responseJson("30",0,"Format error");

        }

        //guardoLog request procesado:
        request.setProcessed(true);
        request = this.requestRepository.save(request);
        LogControl.log("info", "********* Formato OK: Se guardo request procesado." );


            //---------------------------------------------------------//
            //------------Validación de reglas fijas-------------------//
            //
            //Validación de reglas fijas – Si el formato del pedido es correcto se procede a verificar las reglas
            //fijas (ver sección reglas fijas más abajo). Estas reglas se validan en todas las transacciones
            //independientemente de la tarjeta. procesar Transactiondata
            //
            // Fecha de negocio mayor a la fecha de vencimiento de la tarjeta           54 – Expired card
            // Moneda de la transacción igual a moneda de la tarjeta                    13 – Invalid amount
            // Estado de la tarjeta (campo LOCKED_OR_CANCELLED de tabla
            // CARD). Si se trata de una reversa, se debe desestimar esta validación.   62 – Restricted card
            // Saldo suficiente                                                         51 – Insufficient funds
            //
            //---------------------------------------------------------//

            //  Existencia de tarjeta
            //  14 – Invalid card number

        LogControl.log("info", "********* Validación de reglas fijas *********");

        try{
            card=cardRepository.findByCardNumber(transactionDataInDTO.getCardNumber());
        }catch(Exception ex){
            LogControl.log("error", "********* Error: 14 00 Invalid card number   *********");
            LogControl.log("error", ex.getMessage());
            LogControl.log("error", ex.getCause().getMessage());
            LogControl.log("error", ex.getStackTrace().toString());

            return responseJson("14",0,"Invalid card number");
        }

        if(card == null){
            LogControl.log("error", "********* Error: 14 00 Invalid card number   *********");
            return responseJson("14",0,"Invalid card number");
        }

        //---------------------------------------------------------//
        //------------Volcado a TransactionData -------------------//

        //Si el mensaje pasa la validación de formato, esto debe quedar reflejado en la tabla REQUEST
        //en el campo PROCESSED ya que la transacción será procesada. Además, se debe volcar la
        //información del pedido en la tabla TRANSACTION_DATA.

        TransactionData transactionData=mapperTransactionData.map(transactionDataInDTO);
        //agrego alguna cosa que no viene desde el mapperDTO
        transactionData.setRequest(request);
        transactionData.setCard(card);
        //valido pais y moneda contra el sistema
        transactionData.setCountry(merchantCountry);
        transactionData.setCurrency(currency);

        try {
            //  Fecha de negocio mayor a la fecha de vencimiento de la tarjeta
            //  54 – Expired card
            Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            if (card.getDueDate().before(today)) { // si la fecha de vencimiento es anterior a hoy esta vencida
                LogControl.log("error", "********* Error: 54 00 Expired card   *********");
                responseCode="54";
                transactionData.setAuthorizationCode(0);

            }

            //  Moneda de la transacción igual a moneda de la tarjeta
            //  13 – Invalid amount
            if (transactionDataInDTO.getCurrencyCode() != card.getCurrency().getCurrencyCode()) {
                LogControl.log("error", "********* Error: 54 00 Expired card   *********");
                responseCode="13";
                transactionData.setAuthorizationCode(0);

            }

            //  Estado de la tarjeta (campo LOCKED_OR_CANCELLED de tabla CARD).
            //  Si se trata de una reversa, se debe desestimar esta validación.
            //  62 – Restricted card
            if (!esReversa(transactionDataInDTO)) {
                if (card.isLockedOrCanceled()) {
                    //todomaxi
                    // que es una reversa ??

                    //TRANSACTION_IDENTIFIER – Identificador externo de la transacción. Este valor es
                    //igual en transacciones de reversa y compra relacionadas.
                    LogControl.log("error", "    ********* Error: 62 00 Restricted card   *********");
                    responseCode="62";
                    transactionData.setAuthorizationCode(0);

                }
            }

            //  Saldo suficiente
            //  51 – Insufficient funds
            if (transactionDataInDTO.getAmount().compareTo(card.getBalance()) > 1) {
                LogControl.log("error", "******** Error: 51 00 Insufficient funds   *********");
                responseCode="51";
                transactionData.setAuthorizationCode(0);

            }

            LogControl.log("info", "********* Fin validacion reglas fijas ********");
            //---------------------------------------------------------//
            //------------Validación de reglas por tarjeta-------------------//
            //
            LogControl.log("info", "********* Validacion reglas por tarjeta ********");

            if (card.getRuleSet() != null) {
                //valido reglas tarjeta
                RuleSet rs=card.getRuleSet();

                //------INTERNATION_TRX_ENABLED------
                //Si está habilitado, la transacción no puede ser
                //internacional. Validar contra país del sistema.
                //12 – Invalid transaction

                if(rs.getInternationTrxEnabled()!=null){
                    if(rs.getInternationTrxEnabled().booleanValue()){
                        GeneralData generalData= generalDataRepository.findTop1();
                        if(transactionData.getCountry().getIdCountry() != generalData.getIdCountry()){
                            LogControl.log("error", "******** Error: 12 00 Invalid transaction   *********");
                            if(responseCode.equals("00")) {
                                responseCode = "12";
                                transactionData.setAuthorizationCode(0);
                            }

                        }
                    }
                }

                //--------MAX_AMOUNT_PER_TRX--------
                //Cada transacción no puede superar el valor
                //configurado en el campo.
                //68 – Transaction max amount exceeded

                if(rs.getMaxAmountPerTrx() != null){

                    if(transactionData.getAmount().compareTo(rs.getMaxAmountPerTrx()) == 1){
                        LogControl.log("error", "******** Error: 68 00 Transaction max amount exceeded   *********");
                        if(responseCode.equals("00")) {
                            responseCode="68";
                            transactionData.setAuthorizationCode(0);
                        }


                    }
                }

                //--------MAX_SUM_AMOUNT_PER_DAY
                //Monto acumulado diario con la tarjeta no puede
                //superar el valor configurado en el campo.
                //61 – Amount daily limit exceeded
                if(rs.getMaxSumAmountPerDay()!=null){
                    BigDecimal montoAcumuladoDiario=transactionDataRepository.getMontoAcumuladoDiario(card.getIdCard(),transactionData.getTransactionDate());
                    if(montoAcumuladoDiario.compareTo(rs.getMaxSumAmountPerDay()) == 1){
                        LogControl.log("error", "******** Error: 61 00 Amount daily limit exceeded   *********");
                        if(responseCode.equals("00")) {
                            responseCode="61";
                            transactionData.setAuthorizationCode(0);
                        }
                    }
                }

                //---------MAX_COUNT_TRX_PER_DAY
                //Cantidad de transacciones diaria con la tarjeta, no
                //puede superar el valor configurado en el campo.
                //65 – Count daily limit exceeded

                if(rs.getMaxCountTrxPerDay() !=null){
                    int countTrxPerDay=transactionDataRepository.getCountTrxPerDay(card.getIdCard(),transactionData.getTransactionDate());
                    if(countTrxPerDay + 1 > rs.getMaxCountTrxPerDay()) {
                        LogControl.log("error", "******** Error: 65 00 Count daily limit exceeded   *********");
                        if (responseCode.equals("00")) {
                            responseCode = "65";
                            transactionData.setAuthorizationCode(0);
                        }
                    }
                }
            }
            LogControl.log("info", "********* Fin validacion reglas por tarjeta ********");
            //------------Fin de validacion reglas por tarjeta-------------------//

            //----------------fin procesar Transactiondata//

            //todo averiguar de donde sale el authorization code, lo genero yo, la bd ?

            LogControl.log("info", "********* Fin ********");
            if(responseCode.equals("00")){

                //***********************SE AUTORIZÓ LA TRANSACCION******************//

                transactionData.setAuthorizationCode(123456);//todomaxi aca ver lo de autogenerado autorizationcode
            }

        }catch(Exception ex){
            LogControl.log("error:", ex.getMessage());
            LogControl.log("error:", ex.getCause().getMessage());
            LogControl.log("error:", ex.getStackTrace().toString());

        }finally {
            try {


                trResponseCode = responseRepository.findByCode(responseCode);
                transactionData.setResponseCode(trResponseCode);

                //***********************SE GUARDA LA TRANSACCION******************//

                transactionDataRepository.save(transactionData);
                if (transactionData.getAuthorizationCode() !=0 ){

                    //********* se resta saldo de la tarjeta ***********//
                    card.setBalance(card.getBalance().subtract(transactionData.getAmount()));
                    try{

                        //********* se afecta saldo de la tarjeta ***********//

                        cardRepository.save(card);
                        LogControl.log("info", "********* se afectó saldo de la tarjeta ***********");

                        //****************************** ***********//

                    }catch (Exception ex){
                        LogControl.log("error", "********* ERROR : no se afectó saldo de la tarjeta ***********");
                        LogControl.log("error", ex.getMessage());
                        LogControl.log("error", ex.getStackTrace().toString());
                        LogControl.log("error", ex.getCause().getMessage());
                        throw new ProcesadorException("Error no se pudo afectar el saldo de la tarjeta ", HttpStatus.INTERNAL_SERVER_ERROR);
                    }


                }else{
                    LogControl.log("info", "********* NO se afectó saldo de la tarjeta ***********");
                }
                LogControl.log("info", "***********************SE GUARDÓ LA TRANSACCION******************");

                //*****************************************************************//

                return responseJson(trResponseCode.getAuthorizationCode(),transactionData.getAuthorizationCode(), trResponseCode.getDescription());
            }catch (Exception ex){
                LogControl.log("error:", ex.getMessage());
                LogControl.log("error:", ex.getCause().getMessage());
                LogControl.log("error:", ex.getStackTrace().toString());
            }


        }

        return responseJson("12",0, "Invalid transaction");


    }

    private boolean esReversa(TransactionDataInDTO transactionDataInDTO) {
        //todo maxi investigar que es reversa
        return false;
    }

    private String responseJson(String responseCode, int authorizationCode, String description) {
        Gson gson=new Gson();
        ResponseCode rc = new ResponseCode();
        rc.setIdResponseCode(authorizationCode);
        rc.setAuthorizationCode(responseCode);
        rc.setDescription(description);
        return gson.toJson(rc);
    }
}
