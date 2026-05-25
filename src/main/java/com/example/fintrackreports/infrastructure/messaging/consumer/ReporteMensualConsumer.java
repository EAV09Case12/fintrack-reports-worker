package com.example.fintrackreports.infrastructure.messaging.consumer;

import com.example.fintrackreports.application.port.input
        .ReporteMensualUseCasePort;

import com.example.fintrackreports.domain.model
        .ReporteMensualEvent;

import com.example.fintrackreports.infrastructure.config
        .RabbitMQConfig;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReporteMensualConsumer {

    private final ReporteMensualUseCasePort useCase;

    public ReporteMensualConsumer(
            ReporteMensualUseCasePort useCase
    ) {

        this.useCase = useCase;
    }

    @RabbitListener(
            queues = RabbitMQConfig.REPORT_QUEUE
    )
    public void consumirReporteMensual(
            ReporteMensualEvent event
    ) {

        log.info(
                "Mensaje recibido desde RabbitMQ. requestId={}",
                event.getRequestId()
        );

        useCase.procesarReporte(
                event
        );

        log.info(
                "Reporte procesado correctamente. requestId={}",
                event.getRequestId()
        );
    }
}