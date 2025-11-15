package com.backend.sigclc.Model.Resenias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.backend.sigclc.Model.Archivos.ArchivoAdjuntoModel;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document("resenias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReseniaModel {
    @Id
    private ObjectId id;
    private RedactorModel redactor;
    private Date fechaPublicacion;
    private LibroReseniadoModel libro;
    private List<ArchivoAdjuntoModel> archivosAdjuntos = new ArrayList<>();
    private List<ValoracionModel> valoraciones = new ArrayList<>();
    private List<ComentarioModel> comentarios = new ArrayList<>();

    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }

    public Double getCalificacionPromedio() {
        if (valoraciones == null || valoraciones.isEmpty()) {
            return 0.0;
        }
        return valoraciones.stream()
                .mapToDouble(ValoracionModel::getValoracion)
                .average()
                .orElse(0.0);
    }
}