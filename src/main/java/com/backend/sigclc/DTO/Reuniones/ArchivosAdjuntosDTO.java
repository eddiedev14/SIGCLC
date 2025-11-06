package com.backend.sigclc.DTO.Reuniones;

import com.backend.sigclc.Model.Reuniones.Tipo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivosAdjuntosDTO {
    @NotNull(message = "La ruta del archivo es obligatoria")
    @Size(min = 1, max = 100, message = "La ruta debe tener entre {min} y {max} caracteres")
    private String archivoPath;

    @NotNull(message = "El tipo de archivo es obligatorio")
    private Tipo tipo;
}
