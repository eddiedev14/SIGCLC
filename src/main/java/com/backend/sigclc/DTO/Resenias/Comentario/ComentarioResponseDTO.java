package com.backend.sigclc.DTO.Resenias.Comentario;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioResponseDTO {
    private String comentarioId;
    private ComentadorResponseDTO comentador;
    private Date fecha;
    private String comentario;
}
