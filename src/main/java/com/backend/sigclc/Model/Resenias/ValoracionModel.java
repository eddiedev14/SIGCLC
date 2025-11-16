package com.backend.sigclc.Model.Resenias;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValoracionModel {
    private ValoradorModel valorador;
    private Date fecha;
    private Double valoracion;
}
