package com.backend.sigclc.DTO.Resenias.Comentario;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioCreateDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private ObjectId usuarioId;

    @NotNull(message = "El comentario es obligatorio")
    @NotEmpty(message = "El comentario no puede estar vacío")
    @Size(min = 1, max = 200, message = "El comentario debe tener entre 1 y 200 caracteres")
    private String comentario;
}
