package com.backend.sigclc.DTO.Reuniones;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.Model.Reuniones.ModalidadReunion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionUpdateDTO {
    private Date fecha;
    private String hora;
    private ModalidadReunion modalidad;
    private String espacioReunion;
    private List<ObjectId> asistentesId;
    private List<ObjectId> librosSeleccionadosId;
}
