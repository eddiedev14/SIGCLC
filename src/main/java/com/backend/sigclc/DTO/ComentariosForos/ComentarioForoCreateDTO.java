package com.backend.sigclc.DTO.ComentariosForos;

import org.bson.types.ObjectId;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioForoCreateDTO {

    @NotNull(message = "El foro es obligatorio")
    private ObjectId foroId;

    @NotNull(message = "El usuario es obligatorio")
    private ObjectId usuarioId;

    @NotNull(message = "El comentario es obligatorio")
    @Size(min = 1, max = 2000, message = "El comentario debe tener entre 1 y 2000 caracteres")   
    private String comentario;

    // Puede ser null si es comentario raíz
    private ObjectId parentId;
}