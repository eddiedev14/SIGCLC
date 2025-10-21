package com.backend.sigclc.Mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.sigclc.DTO.Usuarios.UsuarioCreateDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioUpdateDTO;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;

@Component
public class UsuarioMapper {
    /**
     * Convierte un UsuarioCreateDTO a UsuariosModel
     */
    public UsuariosModel toModel(UsuarioCreateDTO dto) {
        UsuariosModel model = new UsuariosModel();
        model.setNombreCompleto(dto.getNombreCompleto());
        model.setOcupacion(dto.getOcupacion());
        model.setCorreoElectronico(dto.getCorreoElectronico());
        model.setTelefono(dto.getTelefono());
        model.setRolUsuario(dto.getRolUsuario());
        return model;
    }

    /**
     * Convierte un UsuariosModel a UsuarioResponseDTO
     */
    public UsuarioResponseDTO toResponseDTO(UsuariosModel model) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(model.getIdAsString());
        dto.setNombreCompleto(model.getNombreCompleto());
        dto.setEdad(model.getEdad());
        dto.setOcupacion(model.getOcupacion());
        dto.setCorreoElectronico(model.getCorreoElectronico());
        dto.setTelefono(model.getTelefono());
        dto.setRolUsuario(model.getRolUsuario());
        return dto;
    }

    /**
     * Convierte una lista de UsuariosModel a lista de UsuarioResponseDTO
     */
    public List<UsuarioResponseDTO> toResponseDTOList(List<UsuariosModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Actualiza un UsuariosModel existente con los datos de UsuarioUpdateDTO
     */
    public void updateModelFromDTO(UsuarioUpdateDTO dto, UsuariosModel model) {
        if (dto.getNombreCompleto() != null) model.setNombreCompleto(dto.getNombreCompleto());
        if (dto.getEdad() != null) model.setEdad(dto.getEdad());
        if (dto.getOcupacion() != null) model.setOcupacion(dto.getOcupacion());
        if (dto.getCorreoElectronico() != null) model.setCorreoElectronico(dto.getCorreoElectronico());
        if (dto.getTelefono() != null) model.setTelefono(dto.getTelefono());
    }
}