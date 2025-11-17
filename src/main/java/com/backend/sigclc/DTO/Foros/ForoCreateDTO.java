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

    @NotNull(message = "El nombre del foro es obligatorio")
    @NotBlank(message = "El nombre del foro es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del foro debe tener entre 3 y 100 caracteres")
    private String titulo;

    
    @NotNull(message = "El tipo de temática es obligatorio")
    private TipoTematica tipoTematica;

    @NotNull(message = "La temática es obligatoria")
    @NotBlank(message = "La temática es obligatoria")
    private String tematica;
    
    @NotNull(message = "El moderador es obligatorio")
    private ObjectId moderadorId;
}
