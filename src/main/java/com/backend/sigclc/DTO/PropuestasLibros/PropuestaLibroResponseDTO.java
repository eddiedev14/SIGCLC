package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;
import java.util.List;

import com.backend.sigclc.DTO.PropuestasLibros.LibroPropuesto.LibroPropuestoResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PeriodoSeleccion.PeriodoSeleccionDTO;
import com.backend.sigclc.DTO.PropuestasLibros.UsuarioProponente.UsuarioProponenteResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.Votos.VotoResponseDTO;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropuestaLibroResponseDTO {
    private String id;
    private LibroPropuestoResponseDTO libroPropuesto;
    private UsuarioProponenteResponseDTO usuarioProponente;
    private Date fechaPropuesta;
    private EstadoPropuesta estadoPropuesta;
    private List<VotoResponseDTO> votos;
    private PeriodoSeleccionDTO periodoSeleccion;
}
