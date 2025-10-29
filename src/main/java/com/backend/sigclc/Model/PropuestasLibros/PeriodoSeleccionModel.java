package com.backend.sigclc.Model.PropuestasLibros;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoSeleccionModel {
    private Date fechaInicio;
    private Date fechaFin;
}
