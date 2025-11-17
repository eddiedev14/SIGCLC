package com.backend.sigclc.DTO.RetosLectura;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetoLecturaUpdateDTO {
    @Size(min = 1, max = 100, message = "El titulo debe de tener entre {min} y {max} caracteres")
    private String titulo;

    @Size(min = 1, max = 1000, message = "La descripción debe de tener entre {min} y {max} caracteres")
    private String descripcion;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de inicio del reto de lectura no puede estar en el pasado")  
    private Date fechaInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de finalización del reto de lectura no puede estar en el pasado")  
    private Date fechaFinalizacion;

    private List<ObjectId> librosAsociadosId;

    private List<ObjectId> usuariosInscritosId;

    @AssertTrue(message = "La fecha de finalización debe ser posterior a la fecha de inicio")
    public boolean isFechaFinalizacionPosteriorAFechaInicio() {
        if (fechaInicio == null || fechaFinalizacion == null) {
            return true;
        }
        return fechaFinalizacion.after(fechaInicio);
    }
}
