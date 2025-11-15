package com.backend.sigclc.DTO.Resenias.LibroReseniado;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroReseniadoUpdateDTO {
    private ObjectId libroId;
    
    @Pattern(regexp = "^(?:[1-4](?:\\.5)?|5(?:\\.0)?)$", message = "La calificación debe ser un valor entero o mitad entre 1 y 5 (ej: 1, 1.5, 2, 2.5, etc.)")
    private Double calificacion;
    
    private String opinion;
}
