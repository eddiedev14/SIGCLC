package com.backend.sigclc.Model.Reuniones;

import java.time.LocalTime;
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

@Document ("reuniones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReunionesModel {
    @Id
    private ObjectId id;
    private Date fecha;
    private LocalTime hora;
    private Modalidad modalidad;
    private String espacioReunion;
    private List<LibroSeleccionadoModel> librosSeleccionados = new ArrayList<>();
    private List<AsistenteModel> asistentes = new ArrayList<>();
    private List<ArchivoAdjuntoModel> archivosAdjuntos = new ArrayList<>();

    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }


}
