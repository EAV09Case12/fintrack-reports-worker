package com.example.fintrackreports.infrastructure.cache;

import com.example.fintrackreports.application.port.output
        .ReportCachePort;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisReportCache
        implements ReportCachePort {

    private static final String PREFIX =
            "reportes:";

    private static final Duration TTL =
            Duration.ofHours(2);

    private final RedisTemplate<String, byte[]>
            redisTemplate;

    public RedisReportCache(
            RedisTemplate<String, byte[]>
                    redisTemplate
    ) {

        this.redisTemplate =
                redisTemplate;
    }

    @Override
    public void guardarReporte(
            String requestId,
            byte[] pdf
    ) {

        String key =
                generarKey(requestId);

        redisTemplate.opsForValue().set(
                key,
                pdf,
                TTL
        );
    }

    @Override
    public boolean existeReporte(
            String requestId
    ) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(
                        generarKey(requestId)
                )
        );
    }

    public byte[] obtenerReporte(
            String requestId
    ) {

        return redisTemplate
                .opsForValue()
                .get(
                        generarKey(requestId)
                );
    }

    public void eliminarReporte(
            String requestId
    ) {

        redisTemplate.delete(
                generarKey(requestId)
        );
    }

    private String generarKey(
            String requestId
    ) {

        return PREFIX + requestId;
    }
}