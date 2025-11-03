package com.backend.sigclc.DTO.Libros;

import java.time.Year;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.bson.types.ObjectId;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroCreateDTO {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 1, max = 100, message = "El titulo debe tener entre {min} y {max} caracteres")
    private String titulo;

    @Size(min = 1, message =  "El libro debe de tener almenos un autor")
    private List<@Size(min = 1, max = 100, message = "El autor debe tener entre {min} y {max} caracteres") String> autores;

    @NotNull(message = "El libro debe tener al menos un género")
    private List<GeneroLibro> generos;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1450, message = "El año de publicación debe ser posterior a la invención de la imprenta (1450)")
    private Integer anioPublicacion;

    @NotBlank(message = "La sinopsis es obligatoria")
    @Size(min = 1, max = 1000, message = "La sinopsis debe tener entre {min} y {max} caracteres")
    private String sinopsis;

    private MultipartFile imagen;

    @NotNull(message = "El usuario que registra es obligatorio")
    private ObjectId creadorId;

    @AssertTrue(message = "El año de publicación no puede ser del futuro")
    public boolean isAnioValid(){
        return anioPublicacion == null || anioPublicacion <= Year.now().getValue();
    }
}