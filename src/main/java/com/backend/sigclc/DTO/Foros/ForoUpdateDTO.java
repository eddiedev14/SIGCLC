package com.backend.sigclc.DTO.Foros;

import com.backend.sigclc.Model.Foros.TipoTematica;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForoUpdateDTO {

    private String titulo;

    private TipoTematica tipoTematica;
    
    private String tematica;
}
