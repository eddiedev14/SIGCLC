package com.backend.sigclc.DTO.ComentariosForos;
import com.backend.sigclc.DTO.ComentariosForos.Redactor.RedactorResponseDTO;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioForoResponseDTO {
 private String id;
 private String foroId;
 private RedactorResponseDTO redactor;
 private String comentario;
 private Date fechaPublicacion;
 private String parentId; // null si es raíz
 private List<ComentarioForoResponseDTO> replies; // árbol de respuestas
}