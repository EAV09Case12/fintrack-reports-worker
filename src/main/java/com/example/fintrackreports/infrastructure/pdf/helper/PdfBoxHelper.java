package com.example.fintrackreports.infrastructure.pdf.helper;

import org.apache.pdfbox.pdmodel.font.PDFont;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.Color;

import java.io.IOException;

public class PdfBoxHelper {

    private PdfBoxHelper() {
    }

    public static void escribirTexto(
            PDPageContentStream contentStream,
            String texto,
            PDFont font,
            int fontSize,
            float x,
            float y
    ) throws IOException {

        contentStream.beginText();

        contentStream.setFont(
                font,
                fontSize
        );

        contentStream.newLineAtOffset(
                x,
                y
        );

        contentStream.showText(
                texto
        );

        contentStream.endText();
    }

    public static void escribirTextoColor(
            PDPageContentStream contentStream,
            String texto,
            PDFont font,
            int fontSize,
            float x,
            float y,
            Color color
    ) throws IOException {

        contentStream.setNonStrokingColor(
                color
        );

        escribirTexto(
                contentStream,
                texto,
                font,
                fontSize,
                x,
                y
        );

        contentStream.setNonStrokingColor(
                Color.BLACK
        );
    }

    public static void dibujarLinea(
            PDPageContentStream contentStream,
            float x1,
            float y1,
            float x2,
            float y2
    ) throws IOException {

        contentStream.moveTo(
                x1,
                y1
        );

        contentStream.lineTo(
                x2,
                y2
        );

        contentStream.stroke();
    }

    public static void dibujarRectangulo(
            PDPageContentStream contentStream,
            float x,
            float y,
            float width,
            float height
    ) throws IOException {

        contentStream.addRect(
                x,
                y,
                width,
                height
        );

        contentStream.stroke();
    }

    public static void dibujarRectanguloRelleno(
            PDPageContentStream contentStream,
            float x,
            float y,
            float width,
            float height,
            Color color
    ) throws IOException {

        contentStream.setNonStrokingColor(
                color
        );

        contentStream.addRect(
                x,
                y,
                width,
                height
        );

        contentStream.fill();

        contentStream.setNonStrokingColor(
                Color.BLACK
        );
    }

    public static void escribirTitulo(
            PDPageContentStream contentStream,
            String texto,
            PDFont font,
            float y
    ) throws IOException {

        escribirTexto(
                contentStream,
                texto,
                font,
                18,
                50,
                y
        );
    }

    public static void escribirSubtitulo(
            PDPageContentStream contentStream,
            String texto,
            PDFont font,
            float y
    ) throws IOException {

        escribirTexto(
                contentStream,
                texto,
                font,
                14,
                50,
                y
        );
    }

}