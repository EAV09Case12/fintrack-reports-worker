package com.example.fintrackreports.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String REPORT_QUEUE =
            "report.monthly.queue";

    public static final String REPORT_EXCHANGE =
            "report.monthly.exchange";
    public static final String REPORT_ROUTING_KEY =
            "report.monthly.key";

    @Bean
    public Queue reportQueue() {

        return new Queue(
                REPORT_QUEUE,
                true
        );
    }

    @Bean
    public DirectExchange reportExchange() {
        return new DirectExchange(REPORT_EXCHANGE, true, false);
    }

    @Bean
    public Binding reportBinding(Queue reportQueue, DirectExchange reportExchange) {
        return BindingBuilder
                .bind(reportQueue)
                .to(reportExchange)
                .with(REPORT_ROUTING_KEY);
    }
}