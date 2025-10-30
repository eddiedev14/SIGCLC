package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import com.backend.sigclc.Model.PropuestasLibros.EstadoLectura;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroPropuestoDTO {
    @NotNull(message = "El id del libro es obligatorio")
    private ObjectId libroId;

    //Es opcional
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaSeleccion;

    //Es opcional
    private EstadoLectura estadoLectura;
}
