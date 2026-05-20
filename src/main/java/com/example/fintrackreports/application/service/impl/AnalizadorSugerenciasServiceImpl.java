package com.example.fintrackreports.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.fintrackreports.application.service.AnalizadorSugerenciasService;
import com.example.fintrackreports.domain.model.Categoria;
import com.example.fintrackreports.domain.model.Egreso;
import com.example.fintrackreports.domain.model.PresupuestoMensual;
import com.example.fintrackreports.domain.model.SugerenciaFinanciera;

@Service
public class AnalizadorSugerenciasServiceImpl
        implements AnalizadorSugerenciasService {

    @Override
    public List<SugerenciaFinanciera> generarSugerencias(
            List<Egreso> egresos,
            List<PresupuestoMensual> presupuestos
    ) {

        List<SugerenciaFinanciera> sugerencias = new ArrayList<>();

        for (PresupuestoMensual presupuesto : presupuestos) {

            Categoria categoria = presupuesto.getCategoria();

            List<Egreso> egresosCategoria = egresos.stream()
                    .filter(e -> e.getCategoria() == categoria)
                    .toList();

            BigDecimal totalGastado = egresosCategoria.stream()
                    .map(Egreso::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalGastado.compareTo(presupuesto.getMonto()) > 0) {

                sugerencias.addAll(
                        analizarCategoria(
                                categoria,
                                egresosCategoria,
                                presupuesto
                        )
                );
            }
        }

        return sugerencias;
    }

    private List<SugerenciaFinanciera> analizarCategoria(
            Categoria categoria,
            List<Egreso> egresos,
            PresupuestoMensual presupuesto
    ) {

        Map<String, Integer> frecuenciaDescripcion = new HashMap<>();

        for (Egreso egreso : egresos) {

            String descripcion = egreso.getDescripcion().toLowerCase();

            frecuenciaDescripcion.put(
                    descripcion,
                    frecuenciaDescripcion.getOrDefault(descripcion, 0) + 1
            );
        }

        List<Map.Entry<String, Integer>> descripcionesFrecuentes =
                frecuenciaDescripcion.entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue()
                                .reversed())
                        .limit(3)
                        .collect(Collectors.toList());

        List<SugerenciaFinanciera> sugerencias = new ArrayList<>();

        int prioridad = 1;

        for (Map.Entry<String, Integer> entry : descripcionesFrecuentes) {

            String descripcion = entry.getKey();

            sugerencias.add(
                    new SugerenciaFinanciera(
                            categoria,
                            obtenerTitulo(categoria),
                            construirMensaje(categoria, descripcion),
                            "PREVENCION",
                            prioridad++
                    )
            );
        }

        return sugerencias;
    }

    private String obtenerTitulo(Categoria categoria) {

        return switch (categoria) {

            case ALIMENTACION ->
                    "Controla tus gastos de alimentación";

            case TRANSPORTE ->
                    "Reduce tus gastos de transporte";

            case ENTRETENIMIENTO ->
                    "Modera tus gastos de entretenimiento";

            case SERVICIOS ->
                    "Optimiza el consumo de servicios";

            case SALUD ->
                    "Prevención en gastos de salud";

            case DEUDAS ->
                    "Administra mejor tus deudas";
        };
    }

    private String construirMensaje(
            Categoria categoria,
            String descripcion
    ) {

        return switch (categoria) {

            case ALIMENTACION ->
                    "Se detectaron gastos frecuentes relacionados con \""
                            + descripcion
                            + "\". Intenta comprar productos más económicos sin afectar su calidad.";

            case TRANSPORTE ->
                    "Tus gastos frecuentes en \""
                            + descripcion
                            + "\" están superando el presupuesto. Considera alternativas como caminar, usar bicicleta o transporte más económico.";

            case ENTRETENIMIENTO ->
                    "Los gastos asociados a \""
                            + descripcion
                            + "\" son recurrentes. Intenta moderar la frecuencia de estas actividades.";

            case SERVICIOS ->
                    "Se identificó un gasto constante relacionado con \""
                            + descripcion
                            + "\". Procura reducir el consumo innecesario para equilibrar tus finanzas.";

            case SALUD ->
                    "Se registran múltiples gastos relacionados con \""
                            + descripcion
                            + "\". Mantener hábitos saludables puede ayudarte a disminuir este tipo de gastos.";

            case DEUDAS ->
                    "Los movimientos relacionados con \""
                            + descripcion
                            + "\" son frecuentes. Evita depender excesivamente de créditos o préstamos.";
        };
    }
}