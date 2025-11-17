package com.backend.sigclc.DTO.Resenias.Comentario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentadorResponseDTO {
    private String usuarioId;
    private String nombreCompleto;
}
