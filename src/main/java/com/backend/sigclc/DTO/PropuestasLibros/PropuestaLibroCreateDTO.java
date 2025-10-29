package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropuestaLibroCreateDTO {
    @NotNull(message = "El libro propuesto es obligatorio")
    private LibroPropuestoDTO libroPropuesto;  
    
    @NotNull(message = "El usuario proponente es obligatorio")
    private UsuarioProponenteDTO usuarioProponente;

    @NotNull(message = "La fecha de propuesta es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaPropuesta;

    @NotNull(message = "El estado de la propuesta es obligatorio")
    private EstadoPropuesta estadoPropuesta;

    // Los votos son opcionales al momento de crear la propuesta
    private List<VotoDTO> votos;

    // El periodo de seleccion al momento de crear la propuesta es opcional
    private PeriodoSeleccionDTO periodoSeleccion;
}
