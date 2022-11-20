package com.example.procesador.persistence.repository;

import com.example.procesador.persistence.entity.TransactionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;

public interface TransactionDataRepository extends JpaRepository<TransactionData, Integer> {

    @Query(value = "SELECT SUM(amount) FROM TRANSACTION_DATA WHERE id_card=?1 and transaction_date = ?2", nativeQuery = true)
    BigDecimal getMontoAcumuladoDiario(int idCard, Date transactionDate);

    @Query(value = "select count(*) from TRANSACTION_DATA where id_card=?1 and transaction_date = ?2",nativeQuery = true)
    int getCountTrxPerDay(Integer idCard, Date transactionDate);

//    @Query(value = "Select ID_COUNTRY, MERCHANT_NAME,  ID_RESPONSE_CODE code, count(*) cantidad from TRANSACTION_DATA\n" +
//            "where \n" +
//            "transaction_date=?1 \n" +
//            "group by ID_COUNTRY,MERCHANT_NAME,ID_RESPONSE_CODE for json path, root ('transaccionesProcesadas')",nativeQuery = true)
    @Query(value = "Select\tt_country.COUNTRY_NAME,t_tr.MERCHANT_NAME,  t_co.CODE CODE, count(*) \"COUNT\" from TRANSACTION_DATA t_tr,RESPONSE_CODE t_co,COUNTRY t_country where t_country.ID_COUNTRY=t_tr.ID_COUNTRY and t_co.ID_RESPONSE_CODE=t_tr.ID_RESPONSE_CODE and t_tr.transaction_date=?1 group by COUNTRY_NAME,MERCHANT_NAME,CODE for json path, root ('transaccionesProcesadas')",nativeQuery = true)
    String getTransactionDataByTransactionDate(Date transactionDate);
}
