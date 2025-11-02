package com.backend.sigclc.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroUpdateDTO;

import com.backend.sigclc.Model.PropuestasLibros.LibroPropuestoModel;
import com.backend.sigclc.DTO.PropuestasLibros.LibroPropuestoDTO;
import com.backend.sigclc.DTO.PropuestasLibros.LibroPropuestoResponseDTO;
import com.backend.sigclc.Model.PropuestasLibros.UsuarioProponenteModel;
import com.backend.sigclc.DTO.PropuestasLibros.UsuarioProponenteDTO;
import com.backend.sigclc.DTO.PropuestasLibros.UsuarioProponenteResponseDTO;
import com.backend.sigclc.Model.PropuestasLibros.VotoModel;
import com.backend.sigclc.DTO.PropuestasLibros.VotoDTO;
import com.backend.sigclc.DTO.PropuestasLibros.VotoResponseDTO;
import com.backend.sigclc.Model.PropuestasLibros.PeriodoSeleccionModel;
import com.backend.sigclc.DTO.PropuestasLibros.PeriodoSeleccionDTO;

@Component
public class PropuestaLibroMapper {
    //* Creación
    public PropuestasLibrosModel toModel(PropuestaLibroCreateDTO dto) {
        // Crear modelo a partir del DTO teniendo en cuenta que algunos campos son opcionales (votos, periodoSeleccion)
        PropuestasLibrosModel model = new PropuestasLibrosModel();
        model.setLibroPropuesto(toLibroPropuestoModel(dto.getLibroPropuesto()));
        model.setUsuarioProponente(toUsuarioProponenteModel(dto.getUsuarioProponente()));
        model.setFechaPropuesta(dto.getFechaPropuesta());
        model.setEstadoPropuesta(dto.getEstadoPropuesta());
        
        // Campos opcionales
        model.setVotos(toVotosModelList(dto.getVotos()));
        model.setPeriodoSeleccion(toPeriodoSeleccionModel(dto.getPeriodoSeleccion()));
        return model;
    }

    // Convierte LibroPropuestoDTO a LibroPropuestoModel
    public LibroPropuestoModel toLibroPropuestoModel(LibroPropuestoDTO dto) {
        LibroPropuestoModel model = new LibroPropuestoModel();
        model.setLibroId(dto.getLibroId());
        model.setFechaSeleccion(dto.getFechaSeleccion());
        model.setEstadoLectura(dto.getEstadoLectura());
        return model;
    }

    // Convierte UsuarioProponenteDTO a UsuarioProponenteModel
    public UsuarioProponenteModel toUsuarioProponenteModel(UsuarioProponenteDTO dto) {
        UsuarioProponenteModel model = new UsuarioProponenteModel();
        model.setUsuarioId(dto.getUsuarioId());
        return model;
    }

    // Convierte VotoDTO a VotosModel
    public List<VotoModel> toVotosModelList(List<VotoDTO> dtos) {
        List<VotoModel> votos = new ArrayList<>();
        for (VotoDTO dto : dtos) {
            votos.add(toVotoModel(dto));
        }
        return votos;
    }

    public VotoModel toVotoModel(VotoDTO dto) {
        VotoModel model = new VotoModel();
        model.setUsuarioId(dto.getUsuarioId());
        model.setFechaVoto(dto.getFechaVoto());
        return model;
    }

    // Convierte PeriodoSeleccionDTO a PeriodoSeleccionModel
    public PeriodoSeleccionModel toPeriodoSeleccionModel(PeriodoSeleccionDTO dto) {
        if (dto == null) return null; 
        PeriodoSeleccionModel model = new PeriodoSeleccionModel();
        model.setFechaInicio(dto.getFechaInicio());
        model.setFechaFin(dto.getFechaFin());
        return model;
    }

    //* Responses */

    // Convierte PropuestasLibrosModel a PropuestaLibroResponseDTO
    public PropuestaLibroResponseDTO toResponseDTO(PropuestasLibrosModel model) {
        PropuestaLibroResponseDTO dto = new PropuestaLibroResponseDTO();
        dto.setId(model.getIdAString());
        dto.setLibroPropuesto(toLibroPropuestoResponseDTO(model.getLibroPropuesto()));
        dto.setUsuarioProponente(toUsuarioProponenteResponseDTO(model.getUsuarioProponente()));
        dto.setFechaPropuesta(model.getFechaPropuesta());
        dto.setEstadoPropuesta(model.getEstadoPropuesta());
        dto.setVotos(toVotosResponseDTOList(model.getVotos()));
        dto.setPeriodoSeleccion(toPeriodoSeleccionDTO(model.getPeriodoSeleccion()));
        return dto;
    }

    // Convierte List<PropuestasLibrosModel> a List<PropuestaLibroResponseDTO>
    public List<PropuestaLibroResponseDTO> toResponseDTOList(List<PropuestasLibrosModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Convierte LibroPropuestoModel a LibroPropuestoResponseDTO
    public LibroPropuestoResponseDTO toLibroPropuestoResponseDTO(LibroPropuestoModel model) {
        LibroPropuestoResponseDTO dto = new LibroPropuestoResponseDTO();
        dto.setLibroId(model.getLibroPropuestoIdAsString());
        dto.setTitulo(model.getTitulo());
        dto.setGeneros(model.getGeneros());
        dto.setFechaSeleccion(model.getFechaSeleccion());
        dto.setEstadoLectura(model.getEstadoLectura());
        return dto;
    }

    // Convierte UsuarioProponenteModel a UsuarioProponenteResponseDTO
    public UsuarioProponenteResponseDTO toUsuarioProponenteResponseDTO(UsuarioProponenteModel model) {
        UsuarioProponenteResponseDTO dto = new UsuarioProponenteResponseDTO();
        dto.setUsuarioId(model.getUsuarioIdAsString());
        dto.setNombreCompleto(model.getNombreCompleto());
        return dto;
    }

    // Convierte VotosModel a VotoResponseDTO
    public VotoResponseDTO toVotoResponseDTO(VotoModel model) {
        VotoResponseDTO dto = new VotoResponseDTO();
        dto.setUsuarioId(model.getUsuarioIdAsString());
        dto.setNombreCompleto(model.getNombreCompleto());
        dto.setFechaVoto(model.getFechaVoto());
        return dto;
    }

    // Convierte List<VotosModel> a List<VotoResponseDTO>
    public List<VotoResponseDTO> toVotosResponseDTOList(List<VotoModel> models) {
        return models.stream()
                .map(this::toVotoResponseDTO)
                .toList();
    }

    // Convierte PeriodoSeleccionModel a PeriodoSeleccionDTO
    public PeriodoSeleccionDTO toPeriodoSeleccionDTO(PeriodoSeleccionModel model) {
        if (model == null) return null;
        PeriodoSeleccionDTO dto = new PeriodoSeleccionDTO();
        dto.setFechaInicio(model.getFechaInicio());
        dto.setFechaFin(model.getFechaFin());
        return dto;
    }

    //* Update DTO */
    public void updateModelFromDTO(PropuestaLibroUpdateDTO dto, PropuestasLibrosModel model) {
        if (dto.getLibroPropuesto() != null) model.setLibroPropuesto(toLibroPropuestoModel(dto.getLibroPropuesto()));
        if (dto.getUsuarioProponente() != null) model.setUsuarioProponente(toUsuarioProponenteModel(dto.getUsuarioProponente()));
        if (dto.getFechaPropuesta() != null) model.setFechaPropuesta(dto.getFechaPropuesta());
        if (dto.getEstadoPropuesta() != null) model.setEstadoPropuesta(dto.getEstadoPropuesta());
        if (dto.getVotos() != null) model.setVotos(toVotosModelList(dto.getVotos()));
        if (dto.getPeriodoSeleccion() != null) model.setPeriodoSeleccion(toPeriodoSeleccionModel(dto.getPeriodoSeleccion()));
    }
}