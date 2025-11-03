package com.backend.sigclc.DTO.Libros;

import java.time.Year;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroUpdateDTO {

    @Size(min = 1, max = 100, message = "El titulo debe tener entre {min} y {max} caracteres")
    private String titulo;

    private List<@Size(min = 1, max = 100, message = "El autor debe tener entre {min} y {max} caracteres") String> autores;

    private List<GeneroLibro> generos;

    @Min(value = 1450, message = "El año de publicación debe ser posterior a la invención de la imprenta (1450)")
    private Integer anioPublicacion;

    @Size(min = 1, max = 1000, message = "La sinopsis debe tener entre {min} y {max} caracteres")
    private String sinopsis;

    @Size(max = 300, message = "La ubicación del archivo tener menos de {max} caracteres")
    private String portadaPath;

    private MultipartFile imagen;

    @AssertTrue(message = "El año de publicación no puede ser del futuro")
    public boolean isAnioValid(){
        return anioPublicacion == null || anioPublicacion <= Year.now().getValue();
    }
}
