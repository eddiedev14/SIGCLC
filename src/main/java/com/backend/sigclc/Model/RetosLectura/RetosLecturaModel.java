package com.backend.sigclc.Model.RetosLectura;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("retosLectura")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetosLecturaModel {
    @Id
    private ObjectId id;
    private String titulo;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFinalizacion;
    private List<LibrosAsociadosModel> librosAsociados = new ArrayList<>();
    private List<UsuariosInscritosModel> usuariosInscritos = new ArrayList<>();

    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }
}
