package com.example.fintrackreports.infrastructure.pdf.generator;

import com.example.fintrackreports.domain.model.BalanceFinanciero;
import com.example.fintrackreports.domain.model.Categoria;
import com.example.fintrackreports.domain.model.ReporteFinanciero;
import com.example.fintrackreports.domain.model.ReporteMensualEvent;
import com.example.fintrackreports.domain.model.SugerenciaFinanciera;

import com.example.fintrackreports.infrastructure.messaging.consumer
        .ReporteMensualConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import java.nio.file.Files;
import java.nio.file.Path;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfGeneratorImplTest {

    @Test
    void deberiaSimularLecturaCompletaMensajeRabbitMQ()
            throws Exception {

        String json = """
                {
                  "requestId": "REQ-001",
                  "emailUsuario": "usuario@test.com",
                  "mes": 5,
                  "anio": 2026,
                  "egresos": [
                    {
                      "id": 1,
                      "monto": 150000,
                      "fecha": "2026-05-10",
                      "categoria": "ALIMENTACION",
                      "descripcion": "Compra supermercado",
                      "emailUsuario": "usuario@test.com"
                    },
                    {
                      "id": 2,
                      "monto": 80000,
                      "fecha": "2026-05-12",
                      "categoria": "TRANSPORTE",
                      "descripcion": "Gasolina carro",
                      "emailUsuario": "usuario@test.com"
                    },
                    {
                      "id": 3,
                      "monto": 120000,
                      "fecha": "2026-05-15",
                      "categoria": "ENTRETENIMIENTO",
                      "descripcion": "Salida cine",
                      "emailUsuario": "usuario@test.com"
                    }
                  ],
                  "presupuestos": [
                    {
                      "fecha": "2026-05-01",
                      "categoria": "ALIMENTACION",
                      "monto": 100000
                    },
                    {
                      "fecha": "2026-05-01",
                      "categoria": "TRANSPORTE",
                      "monto": 50000
                    },
                    {
                      "fecha": "2026-05-01",
                      "categoria": "ENTRETENIMIENTO",
                      "monto": 70000
                    }
                  ]
                }
                """;

        ObjectMapper objectMapper =
                new ObjectMapper();

        objectMapper.registerModule(
                new JavaTimeModule()
        );

        ReporteMensualEvent event =
                objectMapper.readValue(
                        json,
                        ReporteMensualEvent.class
                );

        ReporteMensualConsumer consumer =
                new ReporteMensualConsumer(
                        reporteEvent -> {

                            ReporteFinanciero reporte =
                                    construirReporte(
                                            reporteEvent
                                    );

                            PdfGeneratorImpl generator =
                                    new PdfGeneratorImpl();

                            byte[] pdf =
                                    generator.generarReporte(
                                            reporte
                                    );

                            Path path =
                                    Path.of(
                                            "worker-reporte-test.pdf"
                                    );

                            try {

                                Files.write(
                                        path,
                                        pdf
                                );

                                } catch (Exception e) {

                                throw new RuntimeException(e);
                                }

                            System.out.println(
                                    "PDF generado correctamente."
                            );

                            System.out.println(
                                    "Ruta archivo: "
                                            + path.toAbsolutePath()
                            );

                            System.out.println(
                                    "Tamaño PDF bytes: "
                                            + pdf.length
                            );

                            assertTrue(
                                    pdf.length > 0
                            );
                        }
                );

        assertDoesNotThrow(() ->
                consumer.consumirReporteMensual(
                        event
                )
        );
    }

    private ReporteFinanciero construirReporte(
            ReporteMensualEvent event
    ) {

        ReporteFinanciero reporte =
                new ReporteFinanciero();

        reporte.setRequestId(
                event.getRequestId()
        );

        reporte.setEmailUsuario(
                event.getEmailUsuario()
        );

        reporte.setMes(
                event.getMes()
        );

        reporte.setAnio(
                event.getAnio()
        );

        reporte.setEgresos(
                event.getEgresos()
        );

        reporte.setPresupuestos(
                event.getPresupuestos()
        );

        List<BalanceFinanciero> balances =
                new ArrayList<>();

        BalanceFinanciero balance1 =
                new BalanceFinanciero();

        balance1.setCategoria(
                Categoria.ALIMENTACION
        );

        balance1.setPresupuesto(
                BigDecimal.valueOf(100000)
        );

        balance1.setGastoReal(
                BigDecimal.valueOf(150000)
        );

        balance1.setDiferencia(
                BigDecimal.valueOf(-50000)
        );

        balance1.setPorcentajeUso(
                150.0
        );

        balances.add(balance1);

        BalanceFinanciero balance2 =
                new BalanceFinanciero();

        balance2.setCategoria(
                Categoria.TRANSPORTE
        );

        balance2.setPresupuesto(
                BigDecimal.valueOf(50000)
        );

        balance2.setGastoReal(
                BigDecimal.valueOf(80000)
        );

        balance2.setDiferencia(
                BigDecimal.valueOf(-30000)
        );

        balance2.setPorcentajeUso(
                160.0
        );

        balances.add(balance2);

        BalanceFinanciero balance3 =
        new BalanceFinanciero();

        balance3.setCategoria(
                Categoria.ENTRETENIMIENTO
        );

        balance3.setPresupuesto(
                BigDecimal.valueOf(70000)
        );

        balance3.setGastoReal(
                BigDecimal.valueOf(120000)
        );

        balance3.setDiferencia(
                BigDecimal.valueOf(-50000)
        );

        balance3.setPorcentajeUso(
                171.42
        );

        balances.add(balance3);

        reporte.setBalances(
                balances
        );

        List<SugerenciaFinanciera> sugerencias =
                new ArrayList<>();

        sugerencias.add(
                new SugerenciaFinanciera(
                        Categoria.ALIMENTACION,
                        "Controla gastos",
                        "Reduce compras innecesarias.",
                        "PREVENCION",
                        1
                )
        );

        sugerencias.add(
                new SugerenciaFinanciera(
                        Categoria.TRANSPORTE,
                        "Optimiza transporte",
                        "Usa medios alternativos.",
                        "PREVENCION",
                        2
                )
        );

        reporte.setSugerencias(
                sugerencias
        );

        return reporte;
    }
}