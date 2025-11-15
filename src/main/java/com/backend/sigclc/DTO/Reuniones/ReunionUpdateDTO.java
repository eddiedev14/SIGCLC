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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReunionUpdateDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de la reunión no puede ser en el pasado")  
    private Date fecha;

    @NotNull(message = "La hora de la reunión es obligatoria")
    @Pattern(
        regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$",
        message = "La hora debe tener el formato HH:mm (por ejemplo, 14:30)"
    )
    private String hora;
    
    private ModalidadReunion modalidad;
    private String espacioReunion;
    private List<ObjectId> asistentesId;
    private List<ObjectId> librosSeleccionadosId;
    private List<MultipartFile> archivosAdjuntos;
}
