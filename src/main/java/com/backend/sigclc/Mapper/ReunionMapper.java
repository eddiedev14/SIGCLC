package com.backend.sigclc.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionUpdateDTO;
import com.backend.sigclc.Model.Reuniones.ArchivosAdjuntosModel;
import com.backend.sigclc.Model.Reuniones.AsistentesModel;
import com.backend.sigclc.Model.Reuniones.LibrosSeleccionadosModel;
import com.backend.sigclc.Model.Reuniones.ReunionesModel;
import com.backend.sigclc.DTO.Reuniones.ArchivosAdjuntosResponseDTO;
import com.backend.sigclc.DTO.Reuniones.AsistentesResponseDTO;
import com.backend.sigclc.DTO.Reuniones.LibrosSeleccionadosResponseDTO;

public class ReunionMapper {
    
    public ReunionesModel toModel(ReunionCreateDTO dto) {
        ReunionesModel model = new ReunionesModel();
        model.setFecha(dto.getFecha());
        model.setHora(dto.getHora());
        model.setModalidad(dto.getModalidad());
        model.setEspacioReunion(dto.getEspacioReunion());
        //No sé que mas va acá
        return model;
    }    

    // Convierte ReunionesModel a ReunionResponseDTO
    public ReunionResponseDTO toResponseDTO(ReunionesModel model) {
        ReunionResponseDTO dto = new ReunionResponseDTO();
        dto.setId(model.getIdAString());
        dto.setFecha(model.getFecha());
        dto.setHora(model.getHora());
        dto.setModalidad(model.getModalidad());
        dto.setEspacioReunion(model.getEspacioReunion());
        dto.setLibrosSeleccionados(toLibrosSeleccionadosResponseDTOList(model.getLibrosSeleccionados()));
        dto.setAsistentes(toAsistentesResponseDTOList(model.getAsistentes()));
        dto.setArchivosAdjuntos(toArchivosAdjuntosResponseDTOList(model.getArchivosAdjuntos()));
        return dto;
    }

    // Convierte List<ReunionesModel> a List<ReunionResponseDTO>
    public List<ReunionResponseDTO> toResponseDTOList(List<ReunionesModel> models) {
        if (models == null) return new ArrayList<>();
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Convierte List<ReunionesModel> a List<ReunionResponseDTO>
    public List<ReunionResponseDTO> toReunionesResponseDTOList(List<ReunionesModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    

    // Convierte ArchivosAdjutosModel a ArchivsAdjuntosResponseDTO
    public ArchivosAdjuntosResponseDTO toArchivosAdjuntosResponseDTO(ArchivosAdjuntosModel model) {
        ArchivosAdjuntosResponseDTO dto = new ArchivosAdjuntosResponseDTO();
        dto.setArchivoPath(model.getArchivoPath());
        dto.setTipo(model.getTipo());
        return dto;
    }

    // Convierte List<ArchivosAdjuntosModel> a List<ArchivosAdjuntosResponseDTO>
    public List<ArchivosAdjuntosResponseDTO> toArchivosAdjuntosResponseDTOList(List<ArchivosAdjuntosModel> models) {
        return models.stream()
                .map(this::toArchivosAdjuntosResponseDTO)
                .toList();
    }

    // Convierte AsistentesModel a AsistentesResponseDTO
    public AsistentesResponseDTO toAsistentesResponseDTO(AsistentesModel model) {
        AsistentesResponseDTO dto = new AsistentesResponseDTO();
        dto.setAsistenteId(model.getAsistenteIdAString());
        dto.setNombreCompleto(model.getNombreCompleto());
        return dto;
    }

    // Convierte List<AsistentesModel> a List<AsistentesResponseDTO>
    public List<AsistentesResponseDTO> toAsistentesResponseDTOList(List<AsistentesModel> models) {
        return models.stream()
                .map(this::toAsistentesResponseDTO)
                .toList();
    }

    // Convierte LibrosSeleccionadosModel a LibrosSeleccionadosResponseDTO
    public LibrosSeleccionadosResponseDTO toLibrosSeleccionadosResponseDTO(LibrosSeleccionadosModel model) {
        LibrosSeleccionadosResponseDTO dto = new LibrosSeleccionadosResponseDTO();
        dto.setPropuestaId(model.getPropuestaIdAString());
        dto.setTitulo(model.getTitulo());
        dto.setGeneros(model.getGeneros());
        return dto;
    }

    // Convierte List<LibrosSeleccionadosModel> a List<LibrosSeleccionadosResponseDTO>
    public List<LibrosSeleccionadosResponseDTO> toLibrosSeleccionadosResponseDTOList(List<LibrosSeleccionadosModel> models) {
        return models.stream()
                .map(this::toLibrosSeleccionadosResponseDTO)
                .toList();
    }

    // Convierte LibrosSeleccionadosResponseDTO a LibrosSeleccionadosModel
    public LibrosSeleccionadosModel toLibrosSeleccionadosModel(LibrosSeleccionadosResponseDTO dto) {
        LibrosSeleccionadosModel model = new LibrosSeleccionadosModel();
        model.setPropuestaId(new ObjectId(dto.getPropuestaId()));
        model.setTitulo(dto.getTitulo());
        model.setGeneros(dto.getGeneros());
        return model;
    }

    // Convierte List<LibrosSeleccionadosResponseDTO> a List<LibrosSeleccionadosModel>
    public List<LibrosSeleccionadosModel> toLibrosSeleccionadosModelList(List<LibrosSeleccionadosResponseDTO> dtos) {
        return dtos.stream()
                .map(this::toLibrosSeleccionadosModel)
                .toList();
    }

    // Convierte List<AsistentesResponseDTO> a List<AsistentesModel>
    public AsistentesModel toAsistentesModel(AsistentesResponseDTO dto) {
        AsistentesModel model = new AsistentesModel();
        model.setAsistenteId(new ObjectId(dto.getAsistenteId()));
        model.setNombreCompleto(dto.getNombreCompleto());
        return model;
    }

    // Convierte List<AsistentesResponseDTO> a List<AsistentesModel>
    public List<AsistentesModel> toAsistentesModelList(List<AsistentesResponseDTO> dtos) {
        return dtos.stream()
                .map(this::toAsistentesModel)
                .toList();
    }

    // Convierte List<ArchivosAdjuntosResponseDTO> a List<ArchivosAdjuntosModel>
    public ArchivosAdjuntosModel toArchivosAdjuntosModel(ArchivosAdjuntosResponseDTO dto) {
        ArchivosAdjuntosModel model = new ArchivosAdjuntosModel();
        model.setArchivoPath(dto.getArchivoPath());
        model.setTipo(dto.getTipo());
        return model;
    }

    // Convierte List<ArchivosAdjuntosResponseDTO> a List<ArchivosAdjuntosModel>
    public List<ArchivosAdjuntosModel> toArchivosAdjuntosModelList(List<ArchivosAdjuntosResponseDTO> dtos) {
        return dtos.stream()
                .map(this::toArchivosAdjuntosModel)
                .toList();
    }

    // Actualiza un modelo ReunionesModel a partir de un DTO ReunionUpdateDTO
    public void updateModelFromDTO(ReunionesModel model, ReunionUpdateDTO dto) {
        if (dto.getFecha() != null) model.setFecha(dto.getFecha());
        if (dto.getHora() != null) model.setHora(dto.getHora());
        if (dto.getModalidad() != null) model.setModalidad(dto.getModalidad());
        if (dto.getEspacioReunion() != null) model.setEspacioReunion(dto.getEspacioReunion());
        if (dto.getLibrosSeleccionados() != null) model.setLibrosSeleccionados(toLibrosSeleccionadosModelList(dto.getLibrosSeleccionados()));
        if (dto.getAsistentes() != null) model.setAsistentes(toAsistentesModelList(dto.getAsistentes()));
        if (dto.getArchivosAdjuntos() != null) model.setArchivosAdjuntos(toArchivosAdjuntosModelList(dto.getArchivosAdjuntos()));
    }
}
