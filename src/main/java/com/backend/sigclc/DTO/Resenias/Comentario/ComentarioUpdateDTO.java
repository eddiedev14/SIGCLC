package com.backend.sigclc.DTO.Resenias.Comentario;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioUpdateDTO {
    @NotNull(message = "El comentario es obligatorio")
    @NotEmpty(message = "El comentario no puede estar vacío")
    @Size(min = 1, max = 200, message = "El comentario debe tener entre 1 y 200 caracteres")
    private String comentario;
}
