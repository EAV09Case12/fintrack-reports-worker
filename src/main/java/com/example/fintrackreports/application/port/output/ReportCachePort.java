package com.example.fintrackreports.application.port.output;

public interface ReportCachePort {

    void guardarReporte(
            String requestId,
            byte[] pdf
    );

    boolean existeReporte(
            String requestId
    );

    byte[] obtenerReporte(
            String requestId
    );

    void eliminarReporte(
            String requestId
    );
}