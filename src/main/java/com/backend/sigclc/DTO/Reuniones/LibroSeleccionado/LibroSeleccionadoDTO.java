package com.backend.sigclc.DTO.Reuniones.LibroSeleccionado;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroSeleccionadoDTO {
    @NotNull(message = "El id del libro seleccionado es obligatorio")
    private ObjectId libroSeleccionadoId;
}
