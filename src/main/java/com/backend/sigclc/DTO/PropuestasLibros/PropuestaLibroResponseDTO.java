package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;
import java.util.List;

import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropuestaLibroResponseDTO {
    private String id;
    private LibroPropuestoDTO libroPropuesto;
    private UsuarioProponenteDTO usuarioProponente;
    private Date fechaPropuesta;
    private EstadoPropuesta estadoPropuesta;
    private List<VotoDTO> votos;
    private PeriodoSeleccionDTO periodoSeleccion;
}
