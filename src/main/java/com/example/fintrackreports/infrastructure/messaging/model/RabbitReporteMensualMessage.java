package com.example.fintrackreports.infrastructure.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RabbitReporteMensualMessage {

    private String requestId;

    private String emailUsuario;

    private Integer mes;

    private Integer anio;

    private List<RabbitEgresoMessage> egresos;

    private List<RabbitPresupuestoMessage> presupuestos;

}