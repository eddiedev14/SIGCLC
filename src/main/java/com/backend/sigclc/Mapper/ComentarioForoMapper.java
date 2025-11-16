package com.backend.sigclc.Mapper;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoCreateDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoResponseDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoUpdateDTO;
import com.backend.sigclc.Model.ComentariosForos.ComentarioForoModel;
import com.backend.sigclc.Model.ComentariosForos.RedactorModel;
import com.backend.sigclc.DTO.ComentariosForos.Redactor.RedactorResponseDTO;

@Component
public class ComentarioForoMapper {

    //create

    public ComentarioForoModel toModel(ComentarioForoCreateDTO dto) {
    ComentarioForoModel model = new ComentarioForoModel();
    model.setForoId(dto.getForoId());
    model.setRedactor(toRedactorModel(dto.getUsuarioId()));
    model.setComentario(dto.getComentario());
    model.setParentId(dto.getParentId());
    return model;
    }

    public RedactorModel toRedactorModel(ObjectId redactorId) {
        RedactorModel model = new RedactorModel();
        model.setUsuarioId(redactorId);
        return model;
    }

    //response

    public ComentarioForoResponseDTO toResponseDTO(ComentarioForoModel model) {
        ComentarioForoResponseDTO dto = new ComentarioForoResponseDTO();
        dto.setId(model.getIdAString());
        dto.setForoId(model.getForoIdAString());
        dto.setRedactor(toRedactorDTOResponse(model.getRedactor()));
        dto.setComentario(model.getComentario());
        dto.setFechaPublicacion(model.getFechaPublicacion());
        dto.setParentId(model.getParentIdAsString());
        dto.setReplies(new ArrayList<>());
        return dto;
    }
    

    public RedactorResponseDTO toRedactorDTOResponse(RedactorModel model) {
        RedactorResponseDTO dto = new RedactorResponseDTO();
        dto.setUsuarioId(model.getUsuarioIdAsString());
        dto.setNombreCompleto(model.getNombreCompleto());
        return dto;
    }

     /**
     * Convierte una lista de ForosModel a lista de ForoResponseDTO
     */
    public List<ComentarioForoResponseDTO> toResponseDTOList(List<ComentarioForoModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Actualiza un ForosModel existente con los datos de ForoUpdateDTO
     */
    public void updateModelFromDTO(ComentarioForoUpdateDTO dto, ComentarioForoModel model) {
        if (dto.getComentario() != null) model.setComentario(dto.getComentario());
    }

}
