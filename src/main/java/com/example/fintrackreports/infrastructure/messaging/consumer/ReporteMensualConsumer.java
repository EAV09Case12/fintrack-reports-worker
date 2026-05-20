package com.example.fintrackreports.infrastructure.messaging.consumer;

import com.example.fintrackreports.application.port.input.ReporteMensualUseCasePort;
import com.example.fintrackreports.domain.model.ReporteMensualEvent;

import com.example.fintrackreports.infrastructure.config.RabbitMQConfig;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
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

        useCase.procesarReporte(event);
    }
}
