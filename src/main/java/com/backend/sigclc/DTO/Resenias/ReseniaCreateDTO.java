package com.backend.sigclc.DTO.Resenias;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.backend.sigclc.DTO.Resenias.LibroReseniado.LibroReseniadoCreateDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaCreateDTO {
    @NotNull(message = "El id del redactor es obligatorio")
    private ObjectId redactorId;

    @NotNull(message = "El libro reseniado es obligatorio")
    private LibroReseniadoCreateDTO libroReseniado;

    private List<MultipartFile> archivosAdjuntos;
}
