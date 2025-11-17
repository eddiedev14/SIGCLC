package com.backend.sigclc.DTO.Resenias.Redactor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedactorResponseDTO {
    private String usuarioId;
    private String nombreCompleto;
}
