package com.backend.sigclc.Model.Foros;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document ("foros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForosModel {
    @Id
    private ObjectId id;
    private String titulo;
    private TipoTematica tipoTematica;
    private String tematica;
    private Date fechaPublicacion;
    private ModeradorModel moderador;

    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }
}
