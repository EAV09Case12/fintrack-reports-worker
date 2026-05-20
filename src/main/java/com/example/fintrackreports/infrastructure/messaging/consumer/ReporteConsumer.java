package com.example.fintrackreports.infrastructure.messaging.consumer;

import com.example.fintrackreports.application.port.input
        .ReporteMensualUseCasePort;

import com.example.fintrackreports.domain.model
        .ReporteMensualEvent;

import com.example.fintrackreports.infrastructure.messaging.mapper
        .RabbitEventMapper;

import com.example.fintrackreports.infrastructure.messaging.model
        .RabbitReporteMensualMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

@Component
public class ReporteConsumer {

    private final ReporteMensualUseCasePort
            reporteMensualUseCasePort;

    private final RabbitEventMapper
            rabbitEventMapper;

    public ReporteConsumer(
            ReporteMensualUseCasePort reporteMensualUseCasePort,
            RabbitEventMapper rabbitEventMapper
    ) {

        this.reporteMensualUseCasePort =
                reporteMensualUseCasePort;

        this.rabbitEventMapper =
                rabbitEventMapper;
    }

    @RabbitListener(
            queues = "report.monthly.queue"
    )
    public void consumirReporte(
            RabbitReporteMensualMessage message
    ) {

        ReporteMensualEvent event =
                rabbitEventMapper.toDomain(
                        message
                );

        reporteMensualUseCasePort
                .procesarReporte(
                        event
                );
    }
}