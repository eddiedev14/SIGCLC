package com.backend.sigclc.DTO.Resenias.LibroReseniado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroReseniadoCreateDTO {
    @NotNull(message = "El id del libro es obligatorio")
    private ObjectId libroId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion debe ser mayor o igual a 1")
    @Max(value = 5, message = "La calificacion debe ser menor o igual a 5")
    private Double calificacion;

    @NotNull(message = "La opinion es obligatoria")
    @Size(min = 1, max = 200, message = "La opinion debe tener entre {min} y {max} caracteres")
    private String opinion;
}
