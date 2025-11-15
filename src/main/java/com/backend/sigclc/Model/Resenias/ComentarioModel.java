package com.backend.sigclc.Model.Resenias;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioModel {
    private ComentadorModel comentador;
    private Date fecha;
    private String comentario;
}
