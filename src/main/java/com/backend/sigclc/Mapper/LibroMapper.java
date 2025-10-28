package com.backend.sigclc.Mapper;

import java.util.List;

import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;
import com.backend.sigclc.Model.Libros.LibrosModel;

public class LibroMapper {
    public LibrosModel toModel(LibroCreateDTO dto) {
        LibrosModel model = new LibrosModel();
        model.setTitulo(dto.getTitulo());
        model.setAutor(dto.getAutor());
        model.setGenero(dto.getGenero());
        model.setAnioPublicacion(dto.getAnioPublicacion());
        model.setSinopsis(dto.getSinopsis());
        model.setPortadaPath(dto.getPortadaPath());
        return model;
    }

    public LibroResponseDTO toResponseDTO(LibrosModel model) {
        LibroResponseDTO dto = new LibroResponseDTO();
        dto.setId(model.getIdAString());
        dto.setTitulo(model.getTitulo());
        dto.setAutor(model.getAutor());
        dto.setGenero(model.getGenero());
        dto.setAnioPublicacion(model.getAnioPublicacion());
        dto.setSinopsis(model.getSinopsis());
        dto.setPortadaPath(model.getPortadaPath());
        dto.setRegistrado_por(model.getResgiradoPorAsString());
        return dto;
    }

    public List<LibroResponseDTO> toResponseDTOList(List<LibrosModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    public void updateModelFromDTO(LibroUpdateDTO dto, LibrosModel model) {
        if (dto.getTitulo() != null) model.setTitulo(dto.getTitulo());
        if (dto.getAutor() != null) model.setAutor(dto.getAutor());
        if (dto.getGenero() != null) model.setGenero(dto.getGenero());
        if (dto.getAnioPublicacion() != null) model.setAnioPublicacion(dto.getAnioPublicacion());
        if (dto.getSinopsis() != null) model.setSinopsis(dto.getSinopsis());
        if (dto.getPortadaPath() != null) model.setPortadaPath(dto.getPortadaPath());
    }
}
