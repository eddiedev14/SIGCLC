package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodoSeleccionDTO {
    @NotNull(message = "La fecha de inicio es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;
}
