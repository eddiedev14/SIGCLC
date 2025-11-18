package com.backend.sigclc.Model.RetosLectura;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoLibroModel {
    private Date fecha;
    private String observacion;
}
