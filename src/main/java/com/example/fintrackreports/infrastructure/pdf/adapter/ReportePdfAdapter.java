package com.example.fintrackreports.infrastructure.pdf.adapter;

import com.example.fintrackreports.application.port.output
        .PdfGeneratorPort;

import com.example.fintrackreports.domain.model
        .ReporteFinanciero;

import com.example.fintrackreports.infrastructure.pdf.generator
        .PdfGeneratorImpl;

import org.springframework.stereotype.Component;

@Component
public class ReportePdfAdapter
        implements PdfGeneratorPort {

    private final PdfGeneratorImpl
            pdfGenerator;

    public ReportePdfAdapter(
            PdfGeneratorImpl pdfGenerator
    ) {

        this.pdfGenerator =
                pdfGenerator;
    }

    @Override
    public byte[] generarPdf(
            ReporteFinanciero reporte
    ) {

        return pdfGenerator
                .generarReporte(reporte);
    }

}