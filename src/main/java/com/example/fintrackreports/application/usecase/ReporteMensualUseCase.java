package com.example.fintrackreports.application.usecase;

import com.example.fintrackreports.application.port.input
        .ReporteMensualUseCasePort;

import com.example.fintrackreports.application.port.output
        .PdfGeneratorPort;

import com.example.fintrackreports.application.port.output
        .ReportCachePort;

import com.example.fintrackreports.application.service
        .AnalizadorSugerenciasService;

import com.example.fintrackreports.domain.model
        .BalanceFinanciero;

import com.example.fintrackreports.domain.model
        .Categoria;

import com.example.fintrackreports.domain.model
        .Egreso;

import com.example.fintrackreports.domain.model
        .PresupuestoMensual;

import com.example.fintrackreports.domain.model
        .ReporteFinanciero;

import com.example.fintrackreports.domain.model
        .ReporteMensualEvent;

import com.example.fintrackreports.domain.model
        .SugerenciaFinanciera;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
public class ReporteMensualUseCase
        implements ReporteMensualUseCasePort {

    private final PdfGeneratorPort
            pdfGeneratorPort;

    private final ReportCachePort
            reportCachePort;

    private final AnalizadorSugerenciasService
            analizadorSugerenciasService;

    public ReporteMensualUseCase(
            PdfGeneratorPort pdfGeneratorPort,
            ReportCachePort reportCachePort,
            AnalizadorSugerenciasService
                    analizadorSugerenciasService
    ) {

        this.pdfGeneratorPort =
                pdfGeneratorPort;

        this.reportCachePort =
                reportCachePort;

        this.analizadorSugerenciasService =
                analizadorSugerenciasService;
    }

    @Override
    public void procesarReporte(
            ReporteMensualEvent event
    ) {

        if (
                reportCachePort.existeReporte(
                        event.getRequestId()
                )
        ) {

            return;
        }

        List<Egreso> egresos =
                event.getEgresos();

        List<PresupuestoMensual> presupuestos =
                event.getPresupuestos();

        List<BalanceFinanciero> balances =
                calcularBalances(
                        egresos,
                        presupuestos
                );

        List<SugerenciaFinanciera> sugerencias =
                analizadorSugerenciasService
                        .generarSugerencias(
                                egresos,
                                presupuestos
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

        reporte.setEgresos(
                egresos
        );

        reporte.setPresupuestos(
                presupuestos
        );

        reporte.setBalances(
                balances
        );

        reporte.setSugerencias(
                sugerencias
        );

        byte[] pdf =
                pdfGeneratorPort.generarPdf(
                        reporte
                );

        reportCachePort.guardarReporte(
                event.getRequestId(),
                pdf
        );
    }

    private List<BalanceFinanciero> calcularBalances(
            List<Egreso> egresos,
            List<PresupuestoMensual> presupuestos
    ) {

        List<BalanceFinanciero> balances =
                new ArrayList<>();

        Map<Categoria, List<Egreso>>
                egresosPorCategoria =
                egresos.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Egreso::getCategoria
                                )
                        );

        for (
                PresupuestoMensual presupuesto
                : presupuestos
        ) {

            Categoria categoria =
                    presupuesto.getCategoria();

            BigDecimal presupuestoMonto =
                    presupuesto.getMonto();

            BigDecimal gastoReal =
                    egresosPorCategoria
                            .getOrDefault(
                                    categoria,
                                    List.of()
                            )
                            .stream()
                            .map(Egreso::getMonto)
                            .reduce(
                                    BigDecimal.ZERO,
                                    BigDecimal::add
                            );

            BigDecimal diferencia =
                    presupuestoMonto.subtract(
                            gastoReal
                    );

            Double porcentajeUso =
                    0.0;

            if (
                    presupuestoMonto.compareTo(
                            BigDecimal.ZERO
                    ) > 0
            ) {

                porcentajeUso =
                        gastoReal
                                .multiply(
                                        BigDecimal.valueOf(
                                                100
                                        )
                                )
                                .divide(
                                        presupuestoMonto,
                                        2,
                                        RoundingMode.HALF_UP
                                )
                                .doubleValue();
            }

            BalanceFinanciero balance =
                    new BalanceFinanciero();

            balance.setCategoria(
                    categoria
            );

            balance.setPresupuesto(
                    presupuestoMonto
            );

            balance.setGastoReal(
                    gastoReal
            );

            balance.setDiferencia(
                    diferencia
            );

            balance.setPorcentajeUso(
                    porcentajeUso
            );

            balances.add(
                    balance
            );
        }

        return balances;
    }
}