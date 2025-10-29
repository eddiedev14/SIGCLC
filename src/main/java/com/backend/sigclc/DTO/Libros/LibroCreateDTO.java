package com.backend.sigclc.DTO.Libros;

import java.time.Year;
import java.util.List;

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

    @NotBlank(message = "El genero es obligatorio")
    @Size(min = 1, max = 100, message = "El genero debe tener entre {min} y {max} caracteres")
    private String genero;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1450, message = "El año de publicación debe ser posterior a la invención de la imprenta (1450)")
    private Integer anioPublicacion;

    @NotBlank(message = "La sinopsis es obligatoria")
    @Size(min = 1, max = 1000, message = "La sinopsis debe tener entre {min} y {max} caracteres")
    private String sinopsis;

    @Size(max = 300, message = "La ubicación del archivo tener menos de {max} caracteres")
    private String portadaPath;

    @NotBlank(message = "El usuario que registra es obligatorio")
    private String registrado_por;

    @AssertTrue(message = "El año de publicación no puede ser del futuro")
    public boolean isAnioValid(){
        return anioPublicacion == null || anioPublicacion <= Year.now().getValue();
    }
}