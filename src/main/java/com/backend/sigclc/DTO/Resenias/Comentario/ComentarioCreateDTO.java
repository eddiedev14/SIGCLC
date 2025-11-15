package com.backend.sigclc.DTO.Resenias.Comentario;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioCreateDTO {
    private ObjectId usuarioId;
    private String comentario;
}
