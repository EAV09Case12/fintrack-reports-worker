package com.example.fintrackreports.infrastructure.cache;

import com.example.fintrackreports.application.port.output
        .ReportCachePort;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

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
            @NonNull RedisTemplate<String, byte[]>
                    redisTemplate
    ) {

        this.redisTemplate =
                Objects.requireNonNull(redisTemplate, "redisTemplate must not be null");
    }

    @Override
    public void guardarReporte(
            String requestId,
            byte[] pdf
    ) {

        Objects.requireNonNull(pdf, "pdf must not be null");

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

    @Nullable
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
                Objects.requireNonNull(requestId, "requestId must not be null");

                return PREFIX + requestId;
    }
}