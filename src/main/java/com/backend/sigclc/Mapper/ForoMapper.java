package com.backend.sigclc.Mapper;


import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.backend.sigclc.DTO.Foros.ForoCreateDTO;
import com.backend.sigclc.DTO.Foros.ForoResponseDTO;
import com.backend.sigclc.DTO.Foros.ForoUpdateDTO;
import com.backend.sigclc.DTO.Foros.Moderador.ModeradorResponseDTO;
import com.backend.sigclc.Model.Foros.ForosModel;
import com.backend.sigclc.Model.Foros.ModeradorModel;

@Component
public class ForoMapper {
    
    //create

    public ForosModel toModel(ForoCreateDTO dto) {
        ForosModel model = new ForosModel();
        model.setTipoTematica(dto.getTipoTematica());
        model.setNombreTematica(dto.getNombreTematica());
        model.setModerador(toModeradorModel(dto.getModeradorId()));
        return model;
    }

    public ModeradorModel toModeradorModel(ObjectId moderadorId) {
        ModeradorModel model = new ModeradorModel();
        model.setModeradorId(moderadorId);
        return model;
    }


    //response

    public ForoResponseDTO toResponseDTO(ForosModel model) {
        ForoResponseDTO dto = new ForoResponseDTO();
        dto.setId(model.getIdAString());
        dto.setTipoTematica(model.getTipoTematica());
        dto.setNombreTematica(model.getNombreTematica());
        dto.setFechaPublicacion(model.getFechaPublicacion());
        dto.setModerador(toModeradorDTOResponse(model.getModerador()));
        return dto;
    }

    public ModeradorResponseDTO toModeradorDTOResponse(ModeradorModel model) {
        ModeradorResponseDTO dto = new ModeradorResponseDTO();
        dto.setModeradorId(model.getModeradorIdAsString());
        dto.setNombreCompleto(model.getNombreCompleto());
        return dto;
    }

    /**
     * Convierte una lista de ForosModel a lista de ForoResponseDTO
     */
    public List<ForoResponseDTO> toResponseDTOList(List<ForosModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Actualiza un ForosModel existente con los datos de ForoUpdateDTO
     */
    public void updateModelFromDTO(ForoUpdateDTO dto, ForosModel model) {
        if (dto.getTipoTematica() != null) model.setTipoTematica(dto.getTipoTematica());
        if (dto.getNombreTematica() != null) model.setNombreTematica(dto.getNombreTematica());
    }

}
