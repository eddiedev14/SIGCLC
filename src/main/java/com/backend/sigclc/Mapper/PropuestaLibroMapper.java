package com.backend.sigclc.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;
import com.backend.sigclc.DTO.PropuestasLibros.LibroPropuestoResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PeriodoSeleccionDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroUpdateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.UsuarioProponenteResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.VotoDTO;
import com.backend.sigclc.DTO.PropuestasLibros.VotoResponseDTO;
import com.backend.sigclc.Model.PropuestasLibros.LibroPropuestoModel;
import com.backend.sigclc.Model.PropuestasLibros.UsuarioProponenteModel;
import com.backend.sigclc.Model.PropuestasLibros.VotoModel;
import com.backend.sigclc.Model.PropuestasLibros.PeriodoSeleccionModel;

@Component
public class PropuestaLibroMapper {
    //* Creación
    public PropuestasLibrosModel toModel(PropuestaLibroCreateDTO dto) {
        // Crear modelo a partir del DTO teniendo en cuenta que algunos campos son opcionales (votos, periodoSeleccion)
        PropuestasLibrosModel model = new PropuestasLibrosModel();
        model.setLibroPropuesto(createLibroPropuestoModel(dto.getLibroId()));
        model.setUsuarioProponente(createUsuarioProponenteModel(dto.getUsuarioId()));
        return model;
    }

    // Crear LibroPropuestoModel
    public LibroPropuestoModel createLibroPropuestoModel(ObjectId libroId) {
        LibroPropuestoModel model = new LibroPropuestoModel();
        model.setLibroId(libroId);
        return model;
    }

    // Crear UsuarioProponenteModel
    public UsuarioProponenteModel createUsuarioProponenteModel(ObjectId usuarioId) {
        UsuarioProponenteModel model = new UsuarioProponenteModel();
        model.setUsuarioId(usuarioId);
        return model;
    }

    // Convertir VotoDTO a VotoModel
    public VotoModel toVotoModel(VotoDTO dto) {
        VotoModel model = new VotoModel();
        model.setUsuarioId(dto.getUsuarioId());
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
        if (models == null) return new ArrayList<>();
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
        if (dto.getEstadoLectura() != null) model.getLibroPropuesto().setEstadoLectura(dto.getEstadoLectura());
        if (dto.getEstadoPropuesta() != null) model.setEstadoPropuesta(dto.getEstadoPropuesta());
        if (dto.getPeriodoSeleccion() != null) model.setPeriodoSeleccion(toPeriodoSeleccionModel(dto.getPeriodoSeleccion()));
    }
}