package com.backend.sigclc.DTO.Reuniones.Asistentes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenteResponseDTO {
    private String asistenteId;
    private String nombreCompleto;
}
