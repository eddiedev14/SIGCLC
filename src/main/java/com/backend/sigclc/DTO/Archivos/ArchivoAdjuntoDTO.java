package com.backend.sigclc.DTO.Archivos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivoAdjuntoDTO {
    @NotNull(message = "La ruta del archivo es obligatoria")
    private MultipartFile archivo;
}
