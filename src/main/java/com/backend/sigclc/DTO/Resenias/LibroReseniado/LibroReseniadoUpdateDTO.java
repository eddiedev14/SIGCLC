package com.backend.sigclc.DTO.Resenias.LibroReseniado;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroReseniadoUpdateDTO {
    private ObjectId libroId;

    @Min(value = 1, message = "La calificacion debe ser mayor o igual a 1")
    @Max(value = 5, message = "La calificacion debe ser menor o igual a 5")
    private Double calificacion;
    
    private String opinion;
}
