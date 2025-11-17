package com.backend.sigclc.DTO.RetosLectura;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetoLecturaCreateDTO {
    @NotNull(message = "El titulo es obligatorio")
    @Size(min = 1, max = 100, message = "El titulo debe de tener entre {min} y {max} caracteres")
    private String titulo;

    @NotNull(message = "La descripción es obligatoria")
    @Size(min = 1, max = 1000, message = "La descripción debe de tener entre {min} y {max} caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de inicio del reto de lectura es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de inicio del reto de lectura no puede estar en el pasado")  
    private Date fechaInicio;

    @NotNull(message = "La fecha de finalización del reto de lectura es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de finalización del reto de lectura no puede estar en el pasado")  
    private Date fechaFinalizacion;

    @NotNull(message = "Los id de los libros asociados son obligatorios")
    private List<ObjectId> librosAsociadosId;

    private List<ObjectId> usuariosInscritosId;

    @AssertTrue(message = "La fecha de finalización debe ser posterior a la fecha de inicio")
    public boolean isFechaFinalizacionPosteriorAFechaInicio() {
        if (fechaInicio == null || fechaFinalizacion == null) {
            return false;
        }
        return fechaFinalizacion.after(fechaInicio);
    }
}
