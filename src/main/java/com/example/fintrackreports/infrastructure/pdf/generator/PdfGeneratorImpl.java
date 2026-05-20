package com.example.fintrackreports.infrastructure.pdf.generator;

import com.example.fintrackreports.domain.model.BalanceFinanciero;
import com.example.fintrackreports.domain.model.ReporteFinanciero;
import com.example.fintrackreports.domain.model.SugerenciaFinanciera;

import com.example.fintrackreports.infrastructure.pdf.helper.PdfBoxHelper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.math.BigDecimal;

@Component
public class PdfGeneratorImpl {

    private static final PDType1Font FONT_NORMAL =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

    private static final PDType1Font FONT_BOLD =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

    private static final PDType1Font FONT_ITALIC =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_OBLIQUE
            );

    public byte[] generarReporte(
            ReporteFinanciero reporte
    ) {

        try (

                PDDocument document =
                        new PDDocument();

                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()

        ) {

            PDPage page =
                    new PDPage(PDRectangle.A4);

            document.addPage(page);

            PDPageContentStream content =
                    new PDPageContentStream(
                            document,
                            page
                    );

            float y = 760;

            y = escribirHeader(
                    content,
                    reporte,
                    y
            );

            y = escribirResumenFinanciero(
                    content,
                    reporte,
                    y
            );

            y = escribirTablaBalances(
                    content,
                    reporte,
                    y
            );

            y = escribirSugerencias(
                    content,
                    reporte,
                    y
            );

            escribirFooter(
                    content,
                    y
            );

            content.close();

            document.save(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error generando PDF",
                    e
            );
        }
    }

    private float escribirHeader(
            PDPageContentStream content,
            ReporteFinanciero reporte,
            float y
    ) throws IOException {

        PdfBoxHelper.escribirTexto(
                content,
                "FINTRACK - REPORTE FINANCIERO",
                FONT_BOLD,
                18,
                50,
                y
        );

        y -= 35;

        PdfBoxHelper.escribirTexto(
                content,
                "Usuario: "
                        + reporte.getEmailUsuario(),
                FONT_NORMAL,
                12,
                50,
                y
        );

        y -= 20;

        PdfBoxHelper.escribirTexto(
                content,
                "Periodo: "
                        + reporte.getMes()
                        + "/"
                        + reporte.getAnio(),
                FONT_NORMAL,
                12,
                50,
                y
        );

        y -= 20;

        PdfBoxHelper.escribirTexto(
                content,
                "Request ID: "
                        + reporte.getRequestId(),
                FONT_NORMAL,
                10,
                50,
                y
        );

        y -= 20;

        PdfBoxHelper.dibujarLinea(
                content,
                50,
                y,
                550,
                y
        );

        return y - 30;
    }

    private float escribirResumenFinanciero(
            PDPageContentStream content,
            ReporteFinanciero reporte,
            float y
    ) throws IOException {

        BigDecimal totalGastado =
                reporte.getBalances()
                        .stream()
                        .map(
                                BalanceFinanciero::getGastoReal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal totalPresupuesto =
                reporte.getBalances()
                        .stream()
                        .map(
                                BalanceFinanciero::getPresupuesto
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal diferencia =
                totalPresupuesto.subtract(
                        totalGastado
                );

        PdfBoxHelper.escribirTexto(
                content,
                "RESUMEN FINANCIERO",
                FONT_BOLD,
                14,
                50,
                y
        );

        y -= 30;

        PdfBoxHelper.escribirTexto(
                content,
                "Total Gastado: $"
                        + totalGastado,
                FONT_NORMAL,
                12,
                60,
                y
        );

        y -= 20;

        PdfBoxHelper.escribirTexto(
                content,
                "Presupuesto Total: $"
                        + totalPresupuesto,
                FONT_NORMAL,
                12,
                60,
                y
        );

        y -= 20;

        PdfBoxHelper.escribirTexto(
                content,
                "Diferencia: $"
                        + diferencia,
                FONT_NORMAL,
                12,
                60,
                y
        );

        return y - 35;
    }

    private float escribirTablaBalances(
            PDPageContentStream content,
            ReporteFinanciero reporte,
            float y
    ) throws IOException {

        PdfBoxHelper.escribirTexto(
                content,
                "BALANCE POR CATEGORIAS",
                FONT_BOLD,
                14,
                50,
                y
        );

        y -= 30;

        for (
                BalanceFinanciero balance
                : reporte.getBalances()
        ) {

            String linea =
                    balance.getCategoria().name()
                            + " | Presupuesto: $"
                            + balance.getPresupuesto()
                            + " | Gastado: $"
                            + balance.getGastoReal()
                            + " | Diferencia: $"
                            + balance.getDiferencia()
                            + " | Uso: "
                            + balance.getPorcentajeUso()
                            + "%";

            PdfBoxHelper.escribirTexto(
                    content,
                    linea,
                    FONT_NORMAL,
                    10,
                    60,
                    y
            );

            y -= 18;
        }

        return y - 30;
    }

    private float escribirSugerencias(
            PDPageContentStream content,
            ReporteFinanciero reporte,
            float y
    ) throws IOException {

        PdfBoxHelper.escribirTexto(
                content,
                "SUGERENCIAS FINANCIERAS",
                FONT_BOLD,
                14,
                50,
                y
        );

        y -= 30;

        for (
                SugerenciaFinanciera sugerencia
                : reporte.getSugerencias()
        ) {

            PdfBoxHelper.escribirTexto(
                    content,
                    "• "
                            + sugerencia.getTitulo(),
                    FONT_BOLD,
                    11,
                    60,
                    y
            );

            y -= 18;

            PdfBoxHelper.escribirTexto(
                    content,
                    sugerencia.getMensaje(),
                    FONT_NORMAL,
                    10,
                    75,
                    y
            );

            y -= 28;
        }

        return y;
    }

    private void escribirFooter(
            PDPageContentStream content,
            float y
    ) throws IOException {

        PdfBoxHelper.dibujarLinea(
                content,
                50,
                y,
                550,
                y
        );

        y -= 20;

        PdfBoxHelper.escribirTexto(
                content,
                "FinTrack Financial Reports Worker",
                FONT_ITALIC,
                10,
                50,
                y
        );
    }
}