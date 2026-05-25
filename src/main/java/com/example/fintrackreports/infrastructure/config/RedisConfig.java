package com.example.fintrackreports.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration();

        config.setHostName(host);

        config.setPort(port);

        if (
                password != null
                && !password.isBlank()
        ) {

            config.setPassword(
                    RedisPassword.of(password)
            );
        }

        return new LettuceConnectionFactory(
                config
        );
    }

    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {

        RedisTemplate<String, byte[]> template =
                new RedisTemplate<>();

        template.setConnectionFactory(
                connectionFactory
        );

        template.setKeySerializer(
                new StringRedisSerializer()
        );

        template.setValueSerializer(
                RedisSerializer.byteArray()
        );

        template.setHashKeySerializer(
                new StringRedisSerializer()
        );

        template.setHashValueSerializer(
                RedisSerializer.byteArray()
        );

        template.afterPropertiesSet();

        return template;
    }
}