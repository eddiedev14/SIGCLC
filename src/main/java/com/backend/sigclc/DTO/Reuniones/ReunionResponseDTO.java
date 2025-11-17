package com.backend.sigclc.DTO.Reuniones;

import java.util.Date;
import java.util.List;

import com.backend.sigclc.DTO.Archivos.ArchivoAdjuntoResponseDTO;
import com.backend.sigclc.DTO.Reuniones.Asistentes.AsistenteResponseDTO;
import com.backend.sigclc.DTO.Reuniones.LibroSeleccionado.LibroSeleccionadoResponseDTO;
import com.backend.sigclc.Model.Reuniones.ModalidadReunion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionResponseDTO {
    private String id;
    private Date fecha;
    private String hora;
    private ModalidadReunion modalidad;
    private String espacioReunion;
    private List<LibroSeleccionadoResponseDTO> librosSeleccionados;
    private List<AsistenteResponseDTO> asistentes;
    private List<ArchivoAdjuntoResponseDTO> archivosAdjuntos;
}
