package com.backend.sigclc.Mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.sigclc.DTO.Archivos.ArchivoAdjuntoResponseDTO;
import com.backend.sigclc.Model.Archivos.ArchivoAdjuntoModel;

@Component
public class ArchivoAdjuntoMapper {
    public ArchivoAdjuntoResponseDTO toArchivoAdjuntoResponseDTO(ArchivoAdjuntoModel model) {
        ArchivoAdjuntoResponseDTO dto = new ArchivoAdjuntoResponseDTO();
        dto.setArchivoPath(model.getArchivoPath());
        dto.setTipo(model.getTipo());
        return dto;
    }

    public List<ArchivoAdjuntoResponseDTO> toArchivosAdjuntosResponseDTOList(List<ArchivoAdjuntoModel> models) {
        return models.stream()
                .map(this::toArchivoAdjuntoResponseDTO)
                .toList();
    }
}
