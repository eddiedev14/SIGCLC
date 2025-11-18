package com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoLibroDTO {
    @NotNull(message = "La fecha de la observación es obligatoria")
    private Date fecha;

    @NotNull(message = "La observación es obligatoria")
    @Size(min = 1, max = 100, message = "La observación debe de tener entre {min} y {max} caracteres")
    private String observacion;
}
