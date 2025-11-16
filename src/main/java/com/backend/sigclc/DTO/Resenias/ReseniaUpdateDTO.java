package com.backend.sigclc.DTO.Resenias;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioCreateDTO;
import com.backend.sigclc.DTO.Resenias.LibroReseniado.LibroReseniadoUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionCreateDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaUpdateDTO {
    private LibroReseniadoUpdateDTO libroReseniado;
    private List<MultipartFile> archivosAdjuntos;
    private List<ValoracionCreateDTO> valoraciones;
    private List<ComentarioCreateDTO> comentarios;
}
