package com.backend.sigclc.DTO.Archivos;

import com.backend.sigclc.Model.Archivos.TipoArchivo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivoAdjuntoResponseDTO {
    private String archivoPath;
    private TipoArchivo tipo;
}
