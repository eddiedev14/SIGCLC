package com.backend.sigclc.DTO.Foros.Moderador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeradorResponseDTO {
    private String moderadorId;
    private String nombreCompleto;
}
