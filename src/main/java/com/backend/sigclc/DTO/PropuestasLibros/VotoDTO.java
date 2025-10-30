package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoDTO {
    @NotNull(message = "El usuario es obligatorio")
    private ObjectId usuarioId;

    @NotNull(message = "La fecha del voto es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaVoto;
}
