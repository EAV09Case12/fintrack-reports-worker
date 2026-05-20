package com.example.fintrackreports.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceFinanciero {

    private Categoria categoria;

    private BigDecimal presupuesto;

    private BigDecimal gastoReal;

    private BigDecimal diferencia;

    private Double porcentajeUso;

}
