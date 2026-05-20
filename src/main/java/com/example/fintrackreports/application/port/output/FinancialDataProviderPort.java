package com.example.fintrackreports.application.port.output;

import com.example.fintrackreports.domain.model.Egreso;
import com.example.fintrackreports.domain.model.PresupuestoMensual;

import java.util.List;

public interface FinancialDataProviderPort {

    List<Egreso> obtenerEgresos(
            String emailUsuario,
            Integer mes,
            Integer anio
    );

    List<PresupuestoMensual> obtenerPresupuestos(
            Integer mes,
            Integer anio
    );

}
