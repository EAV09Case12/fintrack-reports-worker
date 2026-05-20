package com.example.fintrackreports.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SugerenciaFinanciera {

    private Categoria categoria;

    private String titulo;

    private String mensaje;

    private String tipo;

    private Integer prioridad;

}
