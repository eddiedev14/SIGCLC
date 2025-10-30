package com.backend.sigclc.DTO.Libros;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreadorResponseDTO {
    private String usuarioId;
    private String nombreCompleto;
}
