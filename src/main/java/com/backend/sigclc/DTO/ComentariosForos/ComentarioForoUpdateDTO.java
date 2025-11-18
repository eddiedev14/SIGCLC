 package com.backend.sigclc.DTO.ComentariosForos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioForoUpdateDTO {
 @NotBlank(message = "El comentario es obligatorio")
 @Size(min = 1, max = 2000, message = "El comentario debe tener entre 1 y 2000 caracteres")
 private String comentario;
}