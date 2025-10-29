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
    private GeneroLibro genero;
    private Integer anioPublicacion;
    private String sinopsis;
    private String portadaPath;
    private ObjectId registrado_por;
    
    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }

    public String getResgiradoPorAsString(){
        return registrado_por != null ? registrado_por.toHexString():null;
    }
}
