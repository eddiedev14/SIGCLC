package com.backend.sigclc.DTO.PropuestasLibros.LibroPropuesto;

import java.util.Date;

import org.bson.types.ObjectId;

import com.backend.sigclc.Model.PropuestasLibros.EstadoLectura;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroPropuestoUpdateDTO {
    @NotNull(message = "El id del libro es obligatorio")
    private ObjectId libroId;

    // Campos opcionales
    private Date fechaSeleccion;
    private EstadoLectura estadoLectura;
}
