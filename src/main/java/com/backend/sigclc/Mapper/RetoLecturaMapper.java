package com.backend.sigclc.Mapper;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.backend.sigclc.DTO.RetosLectura.RetoLecturaCreateDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaUpdateDTO;
import com.backend.sigclc.DTO.RetosLectura.LibrosAsociados.LibroAsociadoResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.UsuarioInscritoResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoLibroResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoResponseDTO;
import com.backend.sigclc.Model.RetosLectura.LibrosAsociadosModel;
import com.backend.sigclc.Model.RetosLectura.ProgresoLibroModel;
import com.backend.sigclc.Model.RetosLectura.ProgresoModel;
import com.backend.sigclc.Model.RetosLectura.RetosLecturaModel;
import com.backend.sigclc.Model.RetosLectura.UsuariosInscritosModel;

@Component
public class RetoLecturaMapper {
    public RetosLecturaModel toModel(RetoLecturaCreateDTO dto) {
        RetosLecturaModel model = new RetosLecturaModel();
        model.setTitulo(dto.getTitulo());
        model.setDescripcion(dto.getDescripcion());
        model.setFechaInicio(dto.getFechaInicio());
        model.setFechaFinalizacion(dto.getFechaFinalizacion());
        model.setLibrosAsociados(createLibrosAsociadosModelList(dto.getLibrosAsociadosId()));
        return model;
    }



    public RetoLecturaResponseDTO toResponseDTO(RetosLecturaModel model) {
        RetoLecturaResponseDTO dto = new RetoLecturaResponseDTO();
        dto.setId(model.getIdAString());
        dto.setTitulo(model.getTitulo());
        dto.setDescripcion(model.getDescripcion());
        dto.setFechaInicio(model.getFechaInicio());
        dto.setFechaFinalizacion(model.getFechaFinalizacion());
        dto.setLibrosAsociados(toLibrosAsociadosResponseDTOList(model.getLibrosAsociados()));
        dto.setUsuariosInscritos(toUsuarioInscritoResponseDTOList(model.getUsuariosInscritos()));
        return dto;
    }

    public List<RetoLecturaResponseDTO> toResponseDTOList(List<RetosLecturaModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public LibrosAsociadosModel createLibrosAsociadosModel(ObjectId libroId) {
        LibrosAsociadosModel model = new LibrosAsociadosModel();
        model.setLibroId(libroId);
        return model;
    }

    public List<LibrosAsociadosModel> createLibrosAsociadosModelList(List<ObjectId> librosId) {
        return librosId == null ? List.of() : librosId.stream()
                .map(this::createLibrosAsociadosModel)
                .toList();
    }

    public UsuariosInscritosModel createUsuariosInscritosModel(ObjectId usuarioInscritoId) {
        UsuariosInscritosModel model = new UsuariosInscritosModel();
        model.setUsuarioId(usuarioInscritoId);
        return model;
    }

    public List<UsuariosInscritosModel> createUsuariosInscritosModelList(List<ObjectId> usuariosInscritosId) {
        return usuariosInscritosId == null ? List.of() : usuariosInscritosId.stream()
                .map(this::createUsuariosInscritosModel)
                .toList();
    }

    public LibroAsociadoResponseDTO toLibrosAsociadosResponseDTO(LibrosAsociadosModel model) {
        LibroAsociadoResponseDTO dto = new LibroAsociadoResponseDTO();
        dto.setGeneros(model.getGeneros());
        dto.setLibroId(model.getLibroIdAString());
        dto.setTitulo(model.getTitulo());
        return dto;
    }

    public ProgresoResponseDTO toProgresoResponseDTO(ProgresoModel model) {
        ProgresoResponseDTO dto = new ProgresoResponseDTO();
        dto.setLibroAsociadoId(model.getLibroAsociadoIdAString());
        dto.setProgresoLibro(toProgresoLibroResponseDTOList(model.getProgresoLibro()));
        return dto;
    }


    public ProgresoLibroResponseDTO toProgresoLibroResponseDTO(ProgresoLibroModel model){
        ProgresoLibroResponseDTO dto = new ProgresoLibroResponseDTO();
        dto.setFecha(model.getFecha());
        dto.setObservacion(model.getObservacion());
        return dto;
    }

    public UsuarioInscritoResponseDTO toUsuarioInscritoResponseDTO(UsuariosInscritosModel model) {
        UsuarioInscritoResponseDTO dto = new UsuarioInscritoResponseDTO();
        dto.setUsuarioId(model.getUsuarioIdAString());
        dto.setNombreCompleto(model.getNombreCompleto());
        dto.setProgreso(toProgresoResponseDTOList(model.getProgreso()));
        return dto;
    }

    public List<UsuarioInscritoResponseDTO> toUsuarioInscritoResponseDTOList(List<UsuariosInscritosModel> models) {
        return models.stream()
                .map(this::toUsuarioInscritoResponseDTO)
                .toList();
    }

    public List<ProgresoResponseDTO> toProgresoResponseDTOList(List<ProgresoModel> models) {
        return models.stream()
                .map(this::toProgresoResponseDTO)
                .toList();
    }

    public List<ProgresoLibroResponseDTO> toProgresoLibroResponseDTOList(List<ProgresoLibroModel> models) {
        return models.stream()
                .map(this::toProgresoLibroResponseDTO)
                .toList();
    }

    public List<LibroAsociadoResponseDTO> toLibrosAsociadosResponseDTOList(List<LibrosAsociadosModel> models) {
        return models.stream()
                .map(this::toLibrosAsociadosResponseDTO)
                .toList();
    }

    public void updateModelFromDTO(RetosLecturaModel model, RetoLecturaUpdateDTO dto) {
        if (dto.getTitulo() != null) model.setTitulo(dto.getTitulo());
        if (dto.getDescripcion() != null) model.setDescripcion(dto.getDescripcion());
        if (dto.getFechaInicio() != null) model.setFechaInicio(dto.getFechaInicio());
        if (dto.getFechaFinalizacion() != null) model.setFechaFinalizacion(dto.getFechaFinalizacion());
    }
}
