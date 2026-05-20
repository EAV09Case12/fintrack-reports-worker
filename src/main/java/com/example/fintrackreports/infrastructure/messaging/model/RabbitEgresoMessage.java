package com.example.fintrackreports.infrastructure.messaging.model;

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
public class RabbitEgresoMessage {

    private Long id;

    private BigDecimal monto;

    private LocalDate fecha;

    private String categoria;

    private String descripcion;

    private String emailUsuario;

}
