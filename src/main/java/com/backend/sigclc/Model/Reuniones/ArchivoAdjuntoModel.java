package com.backend.sigclc.Model.Reuniones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivoAdjuntoModel {
    private String archivoPath;
    private Tipo tipo;
}
