package com.backend.sigclc.DTO.RetosLectura;

import java.util.Date;
import java.util.List;

import com.backend.sigclc.DTO.RetosLectura.LibrosAsociados.LibroAsociadoResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.UsuarioInscritoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetoLecturaResponseDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFinalizacion;
    private List<LibroAsociadoResponseDTO> librosAsociados;
    private List<UsuarioInscritoResponseDTO> usuariosInscritos;

}
