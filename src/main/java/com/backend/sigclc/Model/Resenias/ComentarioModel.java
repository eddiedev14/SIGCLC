package com.backend.sigclc.Model.Resenias;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioModel {
    private ObjectId comentarioId;
    private ComentadorModel comentador;
    private Date fecha;
    private String comentario;

    public String getComentarioIdAsString() {
        return comentarioId != null ? comentarioId.toHexString():null;
    }
}
