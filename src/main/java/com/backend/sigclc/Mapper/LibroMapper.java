package com.backend.sigclc.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.sigclc.DTO.Libros.CreadorDTO;
import com.backend.sigclc.DTO.Libros.CreadorResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;
import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Model.Libros.CreadorModel;

@Component
public class LibroMapper {
    //* Create */
    public LibrosModel toModel(LibroCreateDTO dto) {
        LibrosModel model = new LibrosModel();
        model.setTitulo(dto.getTitulo());
        model.setAutores(dto.getAutores());
        model.setGeneros(toGenerosModelList(dto.getGeneros()));
        model.setAnioPublicacion(dto.getAnioPublicacion());
        model.setSinopsis(dto.getSinopsis());
        model.setCreador(toCreadorModel(dto.getCreador()));
        return model;
    }

    public List<GeneroLibro> toGenerosModelList(List<GeneroLibro> generos) {
        List<GeneroLibro> generosModel = new ArrayList<>();
        for (GeneroLibro genero : generos) {
            generosModel.add(genero);
        }
        return generosModel;
    }

    public CreadorModel toCreadorModel(CreadorDTO dto) {
        CreadorModel model = new CreadorModel();
        model.setUsuarioId(dto.getUsuarioId());
        return model;
    }

    //* Response */

    public LibroResponseDTO toResponseDTO(LibrosModel model) {
        LibroResponseDTO dto = new LibroResponseDTO();
        dto.setId(model.getIdAString());
        dto.setTitulo(model.getTitulo());
        dto.setAutores(model.getAutores());
        dto.setGeneros(model.getGeneros());
        dto.setAnioPublicacion(model.getAnioPublicacion());
        dto.setSinopsis(model.getSinopsis());
        dto.setPortadaPath(model.getPortadaPath());
        dto.setCreador(toCreadorDTOResponse(model.getCreador()));
        return dto;
    }

    public CreadorResponseDTO toCreadorDTOResponse(CreadorModel model) {
        CreadorResponseDTO dto = new CreadorResponseDTO();
        dto.setUsuarioId(model.getUsuarioIdAsString());
        dto.setNombreCompleto(model.getNombreCompleto());
        return dto;
    }

    public List<LibroResponseDTO> toResponseDTOList(List<LibrosModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //* Update */

    public void updateModelFromDTO(LibroUpdateDTO dto, LibrosModel model) {
        if (dto.getTitulo() != null) model.setTitulo(dto.getTitulo());
        if (dto.getAutores() != null) model.setAutores(dto.getAutores());
        if (dto.getGeneros() != null) model.setGeneros(toGenerosModelList(dto.getGeneros()));
        if (dto.getAnioPublicacion() != null) model.setAnioPublicacion(dto.getAnioPublicacion());
        if (dto.getSinopsis() != null) model.setSinopsis(dto.getSinopsis());

        if (dto.getPortadaPath() != null && !dto.getPortadaPath().isBlank()) {
            model.setPortadaPath(dto.getPortadaPath());
        }
    }
}
