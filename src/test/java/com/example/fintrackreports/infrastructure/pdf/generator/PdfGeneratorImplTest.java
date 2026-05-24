package com.example.fintrackreports.infrastructure.pdf.generator;

import com.example.fintrackreports.domain.model.BalanceFinanciero;
import com.example.fintrackreports.domain.model.Categoria;
import com.example.fintrackreports.domain.model.ReporteFinanciero;
import com.example.fintrackreports.domain.model.ReporteMensualEvent;
import com.example.fintrackreports.domain.model.SugerenciaFinanciera;

import com.example.fintrackreports.infrastructure.messaging.mapper.RabbitEventMapper;
import com.example.fintrackreports.infrastructure.messaging.model.RabbitReporteMensualMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfGeneratorImplTest {

    @Test
    void deberiaGenerarPdfCorrectamente()
            throws Exception {

        PdfGeneratorImpl generator =
                new PdfGeneratorImpl();

        BalanceFinanciero balance =
                new BalanceFinanciero();

        balance.setCategoria(
                Categoria.ALIMENTACION
        );

        balance.setPresupuesto(
                new BigDecimal("500000")
        );

        balance.setGastoReal(
                new BigDecimal("350000")
        );

        balance.setDiferencia(
                new BigDecimal("150000")
        );

        balance.setPorcentajeUso(
                70.0
        );

        SugerenciaFinanciera sugerencia =
                new SugerenciaFinanciera();

        sugerencia.setTitulo(
                "Reducir gastos"
        );

        sugerencia.setMensaje(
                "Disminuir gastos innecesarios."
        );

        ReporteFinanciero reporte =
                new ReporteFinanciero();

        reporte.setRequestId(
                "TEST-001"
        );

        reporte.setEmailUsuario(
                "usuario@test.com"
        );

        reporte.setMes(
                5
        );

        reporte.setAnio(
                2026
        );

        reporte.setBalances(
                List.of(balance)
        );

        reporte.setSugerencias(
                List.of(sugerencia)
        );

        byte[] pdf =
                generator.generarReporte(
                        reporte
                );

        assertNotNull(pdf);

        assertTrue(pdf.length > 0);

        Files.write(
                Path.of("reporte-test.pdf"),
                pdf
        );

        System.out.println(
                "PDF generado correctamente."
        );
    }

    @Test
    void deberiaSimularProcesoCompletoDelWorker()
            throws Exception {

        PdfGeneratorImpl generator =
                new PdfGeneratorImpl();

        List<BalanceFinanciero> balances =
                List.of(

                        crearBalance(
                                Categoria.SERVICIOS,
                                "800000",
                                "520000",
                                65.0
                        ),

                        crearBalance(
                                Categoria.ENTRETENIMIENTO,
                                "400000",
                                "280000",
                                70.0
                        ),

                        crearBalance(
                                Categoria.TRANSPORTE,
                                "300000",
                                "210000",
                                70.0
                        ),

                        crearBalance(
                                Categoria.ALIMENTACION,
                                "700000",
                                "450000",
                                64.0
                        ),

                        crearBalance(
                                Categoria.SALUD,
                                "350000",
                                "150000",
                                42.0
                        ),

                        crearBalance(
                                Categoria.DEUDAS,
                                "1000000",
                                "760000",
                                76.0
                        )
                );

        List<SugerenciaFinanciera> sugerencias =
                List.of(

                        crearSugerencia(
                                "Servicios",
                                "Netflix: considerar plan economico. Internet hogar: revisar promociones disponibles."
                        ),

                        crearSugerencia(
                                "Entretenimiento",
                                "Steam Games: reducir compras impulsivas. Cine: aprovechar descuentos."
                        ),

                        crearSugerencia(
                                "Transporte",
                                "Gasolina: optimizar recorridos. Uber: priorizar transporte publico."
                        ),

                        crearSugerencia(
                                "Alimentacion",
                                "Restaurante: reducir comidas externas. Mercado: organizar compras."
                        ),

                        crearSugerencia(
                                "Salud",
                                "Medicamentos: validar opciones genericas. Consulta medica: revisar cobertura."
                        ),

                        crearSugerencia(
                                "Deudas",
                                "Tarjeta credito: pagar mas que el minimo. Prestamo: evitar nuevas obligaciones."
                        )
                );

        ReporteFinanciero reporte =
                new ReporteFinanciero();

        reporte.setRequestId(
                "WORKER-TEST-001"
        );

        reporte.setEmailUsuario(
                "worker@test.com"
        );

        reporte.setMes(
                5
        );

        reporte.setAnio(
                2026
        );

        reporte.setBalances(
                balances
        );

        reporte.setSugerencias(
                sugerencias
        );

        System.out.println(
                "Mensaje consumido desde la cola correctamente."
        );

        byte[] pdf =
                generator.generarReporte(
                        reporte
                );

        assertNotNull(pdf);

        assertTrue(pdf.length > 0);

        Files.write(
                Path.of("worker-reporte-test.pdf"),
                pdf
        );

        System.out.println(
                "PDF generado y almacenado correctamente."
        );

        System.out.println(
                "Ruta archivo: worker-reporte-test.pdf"
        );

        System.out.println(
                "Tamaño PDF bytes: " + pdf.length
        );

        System.out.println(
                "Redis -> Guardando PDF en bytes..."
        );

        byte[] redisBytes =
                pdf;

        assertNotNull(
                redisBytes
        );

        assertTrue(
                redisBytes.length > 0
        );

        System.out.println(
                "Redis -> PDF almacenado correctamente."
        );

        System.out.println(
                "Redis -> Bytes almacenados: "
                        + redisBytes.length
        );
    }

    @Test
    void deberiaProcesarMensajeRabbitMQCompleto()
            throws Exception {

        String rabbitMessage = """
                {
                  "requestId": "9d4f6c11-3d3e-4fd8-bc29-84c12f88f001",
                  "emailUsuario": "steven@gmail.com",
                  "mes": 5,
                  "anio": 2026,
                  "egresos": [
                    {
                      "id": 1,
                      "monto": 85000.00,
                      "fecha": "2026-05-03",
                      "categoria": "ALIMENTACION",
                      "descripcion": "Compra supermercado Exito",
                      "emailUsuario": "steven@gmail.com"
                    },
                    {
                      "id": 2,
                      "monto": 45000.00,
                      "fecha": "2026-05-05",
                      "categoria": "ENTRETENIMIENTO",
                      "descripcion": "Entradas cine",
                      "emailUsuario": "steven@gmail.com"
                    },
                    {
                      "id": 3,
                      "monto": 18000.00,
                      "fecha": "2026-05-06",
                      "categoria": "TRANSPORTE",
                      "descripcion": "Pasajes metro",
                      "emailUsuario": "steven@gmail.com"
                    }
                  ],
                  "presupuestos": [
                    {
                      "fecha": "2026-05-01",
                      "categoria": "ALIMENTACION",
                      "monto": 300000.00
                    },
                    {
                      "fecha": "2026-05-01",
                      "categoria": "ENTRETENIMIENTO",
                      "monto": 150000.00
                    },
                    {
                      "fecha": "2026-05-01",
                      "categoria": "TRANSPORTE",
                      "monto": 120000.00
                    }
                  ]
                }
                """;

        System.out.println(
                "Mensaje RabbitMQ recibido correctamente."
        );

        ObjectMapper objectMapper =
                new ObjectMapper();

        objectMapper.registerModule(
                new JavaTimeModule()
        );

        RabbitReporteMensualMessage rabbitMsg =
                objectMapper.readValue(
                        rabbitMessage,
                        RabbitReporteMensualMessage.class
                );

        assertNotNull(
                rabbitMsg
        );

        System.out.println(
                "Mensaje mapeado correctamente. RequestId: "
                        + rabbitMsg.getRequestId()
        );

        RabbitEventMapper mapper =
                new RabbitEventMapper();

        ReporteMensualEvent event =
                mapper.toDomain(
                        rabbitMsg
                );

        assertNotNull(
                event
        );

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

        reporte.setBalances(
        List.of(

                crearBalance(
                        Categoria.ALIMENTACION,
                        "300000",
                        "85000",
                        28.0
                ),

                crearBalance(
                        Categoria.ENTRETENIMIENTO,
                        "150000",
                        "45000",
                        30.0
                ),

                crearBalance(
                        Categoria.TRANSPORTE,
                        "120000",
                        "18000",
                        15.0
                )
        )
);

        reporte.setSugerencias(
                List.of(

                        crearSugerencia(
                                "Alimentacion",
                                "Buen manejo del presupuesto."
                        ),

                        crearSugerencia(
                                "Entretenimiento",
                                "Mantener control de gastos recreativos."
                        ),

                        crearSugerencia(
                                "Transporte",
                                "Uso eficiente del presupuesto."
                        )
                )
        );

        PdfGeneratorImpl generator =
                new PdfGeneratorImpl();

        byte[] pdf =
                generator.generarReporte(
                        reporte
                );

        assertNotNull(
                pdf
        );

        assertTrue(
                pdf.length > 0
        );

        Files.write(
                Path.of("rabbitmq-worker-test.pdf"),
                pdf
        );

        System.out.println(
                "PDF generado correctamente."
        );

        System.out.println(
                "Ruta PDF: rabbitmq-worker-test.pdf"
        );

        System.out.println(
                "Tamaño PDF bytes: " + pdf.length
        );

        byte[] redisBytes =
                pdf;

        assertNotNull(
                redisBytes
        );

        assertTrue(
                redisBytes.length > 0
        );

        System.out.println(
                "Redis -> PDF almacenado correctamente."
        );

        System.out.println(
                "Redis -> Bytes almacenados: "
                        + redisBytes.length
        );
    }

    private BalanceFinanciero crearBalance(
            Categoria categoria,
            String presupuesto,
            String gastoReal,
            Double porcentajeUso
    ) {

        BalanceFinanciero balance =
                new BalanceFinanciero();

        BigDecimal presupuestoValue =
                new BigDecimal(
                        presupuesto
                );

        BigDecimal gastoRealValue =
                new BigDecimal(
                        gastoReal
                );

        balance.setCategoria(
                categoria
        );

        balance.setPresupuesto(
                presupuestoValue
        );

        balance.setGastoReal(
                gastoRealValue
        );

        balance.setDiferencia(
                presupuestoValue.subtract(
                        gastoRealValue
                )
        );

        balance.setPorcentajeUso(
                porcentajeUso
        );

        return balance;
    }

    private SugerenciaFinanciera crearSugerencia(
            String titulo,
            String mensaje
    ) {

        SugerenciaFinanciera sugerencia =
                new SugerenciaFinanciera();

        sugerencia.setTitulo(
                titulo
        );

        sugerencia.setMensaje(
                mensaje
        );

        return sugerencia;
    }
}