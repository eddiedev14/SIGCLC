package com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso;

import java.util.List;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoDTO {
    @NotNull(message = "El id del libro asociado es obligatorio")
    private ObjectId libroAsociadoId;
    
    private List<ProgresoLibroDTO> progresoLibro;
}
