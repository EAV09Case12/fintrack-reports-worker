package com.example.fintrackreports.infrastructure.messaging.mapper;

import com.example.fintrackreports.domain.model.Categoria;
import com.example.fintrackreports.domain.model.Egreso;
import com.example.fintrackreports.domain.model.PresupuestoMensual;
import com.example.fintrackreports.domain.model.ReporteMensualEvent;

import com.example.fintrackreports.infrastructure.messaging.model
        .RabbitEgresoMessage;

import com.example.fintrackreports.infrastructure.messaging.model
        .RabbitPresupuestoMessage;

import com.example.fintrackreports.infrastructure.messaging.model
        .RabbitReporteMensualMessage;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitEventMapper {

    public ReporteMensualEvent toDomain(
            RabbitReporteMensualMessage message
    ) {

        ReporteMensualEvent event =
                new ReporteMensualEvent();

        event.setRequestId(
                message.getRequestId()
        );

        event.setEmailUsuario(
                message.getEmailUsuario()
        );

        event.setMes(
                message.getMes()
        );

        event.setAnio(
                message.getAnio()
        );

        event.setEgresos(
                mapEgresos(
                        message.getEgresos()
                )
        );

        event.setPresupuestos(
                mapPresupuestos(
                        message.getPresupuestos()
                )
        );

        return event;
    }

    private List<Egreso> mapEgresos(
            List<RabbitEgresoMessage> egresos
    ) {

        return egresos.stream()
                .map(this::mapEgreso)
                .toList();
    }

    private Egreso mapEgreso(
            RabbitEgresoMessage egreso
    ) {

        Egreso domain =
                new Egreso();

        domain.setId(
                egreso.getId()
        );

        domain.setMonto(
                egreso.getMonto()
        );

        domain.setFecha(
                egreso.getFecha()
        );

        domain.setCategoria(
                Categoria.valueOf(
                        egreso.getCategoria()
                )
        );

        domain.setDescripcion(
                egreso.getDescripcion()
        );

        domain.setEmailUsuario(
                egreso.getEmailUsuario()
        );

        return domain;
    }

    private List<PresupuestoMensual> mapPresupuestos(
            List<RabbitPresupuestoMessage> presupuestos
    ) {

        return presupuestos.stream()
                .map(this::mapPresupuesto)
                .toList();
    }

    private PresupuestoMensual mapPresupuesto(
            RabbitPresupuestoMessage presupuesto
    ) {

        PresupuestoMensual domain =
                new PresupuestoMensual();

        domain.setFecha(
                presupuesto.getFecha()
        );

        domain.setMonto(
                presupuesto.getMonto()
        );

        domain.setCategoria(
                Categoria.valueOf(
                        presupuesto.getCategoria()
                )
        );

        return domain;
    }
}