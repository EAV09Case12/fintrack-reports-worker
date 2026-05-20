package com.example.fintrackreports.application.service;

import java.util.List;

import com.example.fintrackreports.domain.model.Egreso;
import com.example.fintrackreports.domain.model.PresupuestoMensual;
import com.example.fintrackreports.domain.model.SugerenciaFinanciera;

public interface AnalizadorSugerenciasService {

    List<SugerenciaFinanciera> generarSugerencias(
            List<Egreso> egresos,
            List<PresupuestoMensual> presupuestos
    );

}
