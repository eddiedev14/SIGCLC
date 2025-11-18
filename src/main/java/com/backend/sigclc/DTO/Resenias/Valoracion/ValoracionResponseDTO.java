package com.backend.sigclc.DTO.Resenias.Valoracion;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValoracionResponseDTO {
    private ValoradorResponseDTO valorador;
    private Date fecha;
    private Double valoracion;
}
