package com.backend.sigclc.DTO.Foros;

import org.bson.types.ObjectId;

import com.backend.sigclc.Model.Foros.TipoTematica;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForoCreateDTO {
    @NotNull(message = "El tipo de temática es obligatorio")
    private TipoTematica tipoTematica;

    @NotBlank(message = "El nombre de la temática es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre de la temática debe tener entre 3 y 100 caracteres")
    private String nombreTematica;

    @NotNull(message = "El moderador es obligatorio")
    private ObjectId moderadorId;
}
