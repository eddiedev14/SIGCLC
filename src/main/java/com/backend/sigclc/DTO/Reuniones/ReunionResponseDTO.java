package com.backend.sigclc.DTO.Reuniones;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import com.backend.sigclc.Model.Reuniones.Modalidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionResponseDTO {
    private Date fecha;
    private LocalTime hora;
    private Modalidad modalidad;
    private List<LibrosSeleccionadosResponseDTO> librosSeleccionados;
    private List<AsistentesResponseDTO> asistentes;
    private List<ArchivosAdjuntosResponseDTO> archivosAdjuntos;
}
