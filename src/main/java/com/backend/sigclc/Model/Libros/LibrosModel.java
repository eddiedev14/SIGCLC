package com.backend.sigclc.Model.Libros;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document ("libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibrosModel {
    @Id
    private ObjectId id;
    private String titulo;
    private List<String> autores;
    private List<GeneroLibro> generos;
    private Integer anioPublicacion;
    private String sinopsis;
    private String portadaPath;
    private CreadorModel creador;
    
    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }
}
