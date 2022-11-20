package com.example.procesador.mapper;

import com.example.procesador.persistence.entity.Request;
import com.example.procesador.service.dto.RequestInDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Calendar;
@Component
public class RequestInDtoToRequest implements IMapper<RequestInDTO, Request> {
    @Override
    public Request map(RequestInDTO in) {
        Request request = new Request();
        request.setMessage(in.getMessage());
        request.setMessageDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        return request;
    }
}
