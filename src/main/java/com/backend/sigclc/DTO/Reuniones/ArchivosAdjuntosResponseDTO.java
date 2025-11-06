package com.backend.sigclc.DTO.Reuniones;

import com.backend.sigclc.Model.Reuniones.Tipo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivosAdjuntosResponseDTO {
    private String archivoPath;
    private Tipo tipo;
}
