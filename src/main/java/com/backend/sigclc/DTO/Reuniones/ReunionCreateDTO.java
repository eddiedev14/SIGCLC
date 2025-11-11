package com.backend.sigclc.DTO.Reuniones;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.backend.sigclc.Model.Reuniones.ModalidadReunion;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionCreateDTO {
    @NotNull(message = "La fecha de la reunión es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de la reunión no puede ser en el pasado")  
    private Date fecha;

    @NotNull(message = "La hora de la reunión es obligatoria")
    @Pattern(
        regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$",
        message = "La hora debe tener el formato HH:mm (por ejemplo, 14:30)"
    )
    private String hora;

    @NotNull(message = "La modalidad de la reunión es obligatoria")
    private ModalidadReunion modalidad;

    @NotNull(message = "El espacio de la reunión es obligatorio")
    @Size(min = 1, max = 100, message = "El espacio de la reunión debe tener entre {min} y {max} caracteres")
    private String espacioReunion;

    @NotNull(message = "Los id de los libros propuestos son obligatorios")
    private List<ObjectId> librosSeleccionadosId;

    private List<ObjectId> asistentesId;

    private List<MultipartFile> archivosAdjuntos;

}
