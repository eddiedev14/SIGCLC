package com.backend.sigclc.DTO.Reuniones;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionCreateDTO {
    @NotNull(message = "La fecha de la reunión es obligatoria")
    private Date fecha;

    @NotNull(message = "La hora de la reunión es obligatoria")
    private LocalTime hora;

    @NotNull(message = "La modalidad de la reunión es obligatoria")
    private String modalidad;

    @NotNull(message = "El espacio de la reunión es obligatorio")
    @Size(min = 1, max = 100, message = "El espacio de la reunión debe tener entre {min} y {max} caracteres")
    private String espacioReunion;

    @NotNull(message = "Los id de los libros propuestos son obligatorios")
    private List<ObjectId> propuestasId;

    @NotNull(message = "Los id de los asistentes a la reunión son obligatorios")
    private List<ObjectId> asistentesId;

    private List<ArchivosAdjuntosDTO> archivosAdjuntos;

}
