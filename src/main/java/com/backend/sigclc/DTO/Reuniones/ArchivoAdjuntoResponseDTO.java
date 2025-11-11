package com.backend.sigclc.DTO.Reuniones;

import com.backend.sigclc.Model.Reuniones.TipoReunion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivoAdjuntoResponseDTO {
    private String archivoPath;
    private TipoReunion tipo;
}
