package com.example.fintrackreports.application.port.output;

import com.example.fintrackreports.domain.model.ReporteFinanciero;

public interface PdfGeneratorPort {

    byte[] generarPdf(ReporteFinanciero reporte);

}
