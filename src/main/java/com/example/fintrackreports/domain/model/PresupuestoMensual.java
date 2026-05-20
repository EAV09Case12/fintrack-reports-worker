package com.example.fintrackreports.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresupuestoMensual {

    private LocalDate fecha;

    private Categoria categoria;

    private BigDecimal monto;

}