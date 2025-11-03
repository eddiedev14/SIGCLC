package com.backend.sigclc.Model.PropuestasLibros;

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

@Document ("propuestasLibros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropuestasLibrosModel {
    @Id
    private ObjectId id;
    private LibroPropuestoModel libroPropuesto;
    private UsuarioProponenteModel usuarioProponente;
    private Date fechaPropuesta;
    private EstadoPropuesta estadoPropuesta;
    private List<VotoModel> votos = new ArrayList<>();
    private PeriodoSeleccionModel periodoSeleccion = null;
    
    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }
}