package com.backend.sigclc.DTO.Foros;

import java.util.Date;

import com.backend.sigclc.DTO.Foros.Moderador.ModeradorResponseDTO;
import com.backend.sigclc.Model.Foros.TipoTematica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForoResponseDTO {
    private String id;
    private String titulo;
    private TipoTematica tipoTematica;
    private String tematica;
    private Date fechaPublicacion;
    private ModeradorResponseDTO moderador;
}