package com.backend.sigclc.DTO.PropuestasLibros;

import com.backend.sigclc.DTO.PropuestasLibros.PeriodoSeleccion.PeriodoSeleccionDTO;
import com.backend.sigclc.Model.PropuestasLibros.EstadoLectura;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropuestaLibroUpdateDTO {
    //* Solo se puede actualizar: fechaSeleccion (fecha actual), estadoLectura, estadoPropuesta y periodoSeleccion */
    private EstadoLectura estadoLectura;
    private EstadoPropuesta estadoPropuesta;
    private PeriodoSeleccionDTO periodoSeleccion;
}
