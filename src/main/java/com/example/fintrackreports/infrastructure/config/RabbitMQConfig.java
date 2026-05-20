package com.example.fintrackreports.infrastructure.config;

import org.springframework.amqp.core.Queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String REPORT_QUEUE =
            "report.monthly.queue";

    @Bean
    public Queue reportQueue() {

        return new Queue(
                REPORT_QUEUE,
                true
        );
    }
}