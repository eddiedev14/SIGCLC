package com.backend.sigclc.DTO.ComentariosForos.Redactor;

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
