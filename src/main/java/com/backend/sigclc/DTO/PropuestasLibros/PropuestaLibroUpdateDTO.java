package com.backend.sigclc.DTO.PropuestasLibros;

import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropuestaLibroUpdateDTO {
    // TODO: Colocar DTOS de LibroPropuesto y UsuarioProponente de actualizar
    private EstadoPropuesta estadoPropuesta;
    private PeriodoSeleccionDTO periodoSeleccion;
}
