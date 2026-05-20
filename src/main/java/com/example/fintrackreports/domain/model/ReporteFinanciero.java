package com.example.fintrackreports.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteFinanciero {

    private String requestId;

    private String emailUsuario;

    private Integer mes;

    private Integer anio;

    private List<Egreso> egresos;

    private List<PresupuestoMensual> presupuestos;

    private List<BalanceFinanciero> balances;

    private List<SugerenciaFinanciera> sugerencias;

}