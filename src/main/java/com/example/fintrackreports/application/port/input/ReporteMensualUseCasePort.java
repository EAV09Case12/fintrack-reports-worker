package com.example.fintrackreports.application.port.input;

import com.example.fintrackreports.domain.model.ReporteMensualEvent;

public interface ReporteMensualUseCasePort {

    void procesarReporte(ReporteMensualEvent event);

}
