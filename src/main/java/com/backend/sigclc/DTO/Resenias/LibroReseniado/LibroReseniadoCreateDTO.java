package com.backend.sigclc.DTO.Resenias.LibroReseniado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroReseniadoCreateDTO {
    @NotNull(message = "El id del libro es obligatorio")
    private ObjectId libroId;

    @NotNull(message = "La calificacion es obligatoria")
    @Pattern(regexp = "^(?:[1-4](?:\\.5)?|5(?:\\.0)?)$", message = "La calificación debe ser un valor entero o mitad entre 1 y 5 (ej: 1, 1.5, 2, 2.5, etc.)")
    private Double calificacion;

    @NotNull(message = "La opinion es obligatoria")
    @Size(min = 1, max = 200, message = "La opinion debe tener entre {min} y {max} caracteres")
    private String opinion;
}
