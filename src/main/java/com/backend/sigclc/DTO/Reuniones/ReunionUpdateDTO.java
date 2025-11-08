package com.backend.sigclc.DTO.Reuniones;

import java.util.Date;
import java.util.List;

import com.backend.sigclc.Model.Reuniones.Modalidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionUpdateDTO {
    private Date fecha;
    private String hora;
    private Modalidad modalidad;
    private String espacioReunion;
    private List<String> asistentesId;
    private List<String> librosSeleccionadosId;
}
