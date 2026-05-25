package com.example.fintrackreports.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.lang.NonNull;

@Configuration
public class RabbitMQConfig {

    public static final String REPORT_EXCHANGE =
            "fintrack.report.exchange";

    public static final String REPORT_QUEUE =
            "fintrack.report.monthly.queue";

    public static final String REPORT_ROUTING_KEY =
            "report.monthly.generate";

    @Bean
    public TopicExchange reportExchange() {

        return new TopicExchange(
                REPORT_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue reportQueue() {

        return QueueBuilder
                .durable(REPORT_QUEUE)
                .build();
    }

    @Bean
    public Binding reportBinding() {

        return BindingBuilder
                .bind(reportQueue())
                .to(reportExchange())
                .with(REPORT_ROUTING_KEY);
    }

    @Bean
    public @NonNull MessageConverter jsonMessageConverter() {

        ObjectMapper mapper =
                new ObjectMapper();

        mapper.registerModule(
                new JavaTimeModule()
        );

        return new Jackson2JsonMessageConverter(
                mapper
        );
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            @NonNull ConnectionFactory connectionFactory
    ) {

        RabbitTemplate template =
                new RabbitTemplate(
                        connectionFactory
                );

        template.setMessageConverter(
                jsonMessageConverter()
        );

        return template;
    }
}