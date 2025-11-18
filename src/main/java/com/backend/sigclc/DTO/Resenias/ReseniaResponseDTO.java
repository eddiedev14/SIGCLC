package com.backend.sigclc.DTO.Resenias;

import java.util.Date;
import java.util.List;

import com.backend.sigclc.DTO.Archivos.ArchivoAdjuntoResponseDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioResponseDTO;
import com.backend.sigclc.DTO.Resenias.LibroReseniado.LibroReseniadoResponseDTO;
import com.backend.sigclc.DTO.Resenias.Redactor.RedactorResponseDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaResponseDTO {
    private String id;
    private RedactorResponseDTO redactor;
    private Date fechaPublicacion;
    private LibroReseniadoResponseDTO libroReseniado;
    private Double calificacionPromedio;
    private List<ArchivoAdjuntoResponseDTO> archivosAdjuntos;
    private List<ComentarioResponseDTO> comentarios;
    private List<ValoracionResponseDTO> valoraciones;
}
