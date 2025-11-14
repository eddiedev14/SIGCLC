package com.backend.sigclc.Model.Archivos;

import com.backend.sigclc.Model.Reuniones.TipoReunion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivoAdjuntoModel {
    private String archivoPath;
    private TipoReunion tipo;
}
