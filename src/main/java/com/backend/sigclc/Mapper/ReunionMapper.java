package com.backend.sigclc.Mapper;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionUpdateDTO;
import com.backend.sigclc.Model.Reuniones.ArchivoAdjuntoModel;
import com.backend.sigclc.Model.Reuniones.AsistenteModel;
import com.backend.sigclc.Model.Reuniones.LibroSeleccionadoModel;
import com.backend.sigclc.Model.Reuniones.ReunionesModel;
import com.backend.sigclc.DTO.Reuniones.ArchivoAdjuntoResponseDTO;
import com.backend.sigclc.DTO.Reuniones.AsistenteDTO;
import com.backend.sigclc.DTO.Reuniones.AsistenteResponseDTO;
import com.backend.sigclc.DTO.Reuniones.LibroSeleccionadoDTO;
import com.backend.sigclc.DTO.Reuniones.LibroSeleccionadoResponseDTO;

@Component
public class ReunionMapper {
    
    public ReunionesModel toModel(ReunionCreateDTO dto) {
        ReunionesModel model = new ReunionesModel();
        model.setFecha(dto.getFecha());
        model.setHora(dto.getHora());
        model.setModalidad(dto.getModalidad());
        model.setEspacioReunion(dto.getEspacioReunion());
        return model;
    }    

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

    public List<ReunionResponseDTO> toResponseDTOList(List<ReunionesModel> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Convierte LibroSeleccionadoResponseDTO a LibroSeleccionadoModel
    public LibroSeleccionadoModel toLibroSeleccionadoModel(LibroSeleccionadoDTO dto) {
        LibroSeleccionadoModel model = new LibroSeleccionadoModel();
        model.setPropuestaId(dto.getLibroSeleccionadoId());
        return model;
    }

    public AsistenteModel toAsistenteModel(AsistenteDTO dto) {
        AsistenteModel model = new AsistenteModel();
        model.setAsistenteId(dto.getAsistenteId());
        return model;
    }

    public LibroSeleccionadoResponseDTO toLibroSeleccionadoResponseDTO(LibroSeleccionadoModel model) {
        LibroSeleccionadoResponseDTO dto = new LibroSeleccionadoResponseDTO();
        dto.setLibroSeleccionadoId(model.getPropuestaIdAString());
        dto.setTitulo(model.getTitulo());
        dto.setGeneros(model.getGeneros());
        return dto;
    }

    public ArchivoAdjuntoResponseDTO toArchivoAdjuntoResponseDTO(ArchivoAdjuntoModel model) {
        ArchivoAdjuntoResponseDTO dto = new ArchivoAdjuntoResponseDTO();
        dto.setArchivoPath(model.getArchivoPath());
        dto.setTipo(model.getTipo());
        return dto;
    }

    public AsistenteResponseDTO toAsistenteResponseDTO(AsistenteModel model) {
        AsistenteResponseDTO dto = new AsistenteResponseDTO();
        dto.setAsistenteId(model.getAsistenteIdAString());
        dto.setNombreCompleto(model.getNombreCompleto());
        return dto;
    }

    public List<LibroSeleccionadoResponseDTO> toLibrosSeleccionadosResponseDTOList(List<LibroSeleccionadoModel> models) {
        return models.stream()
                .map(this::toLibroSeleccionadoResponseDTO)
                .toList();
    }

    public List<ArchivoAdjuntoResponseDTO> toArchivosAdjuntosResponseDTOList(List<ArchivoAdjuntoModel> models) {
        return models.stream()
                .map(this::toArchivoAdjuntoResponseDTO)
                .toList();
    }

    public List<AsistenteResponseDTO> toAsistentesResponseDTOList(List<AsistenteModel> models) {
        return models.stream()
                .map(this::toAsistenteResponseDTO)
                .toList();
    }

    public List<LibroSeleccionadoModel> toLibroSeleccionadoModelList(List<ObjectId> ids) {
        return ids.stream()
                .map(this::toLibroSeleccionadoModelFromId)
                .toList();
    }

    public List<AsistenteModel> toAsistentesModelList(List<ObjectId> ids) {
        return ids.stream()
                .map(this::toAsistenteModelFromId)
                .toList();
    }

    public AsistenteModel toAsistenteModelFromId(ObjectId asistenteId) {
        AsistenteModel model = new AsistenteModel();
        model.setAsistenteId(asistenteId);
        return model;
    }

    public LibroSeleccionadoModel toLibroSeleccionadoModelFromId(ObjectId libroId) {
        LibroSeleccionadoModel model = new LibroSeleccionadoModel();
        model.setPropuestaId(libroId);
        return model;
    }



    public void updateModelFromDTO(ReunionesModel model, ReunionUpdateDTO dto) {
        if (dto.getFecha() != null) model.setFecha(dto.getFecha());
        if (dto.getHora() != null) model.setHora(dto.getHora());
        if (dto.getModalidad() != null) model.setModalidad(dto.getModalidad());
        if (dto.getEspacioReunion() != null) model.setEspacioReunion(dto.getEspacioReunion());
        if (dto.getLibrosSeleccionadosId() != null) model.setLibrosSeleccionados(toLibroSeleccionadoModelList(dto.getLibrosSeleccionadosId()));
        if (dto.getAsistentesId() != null) model.setAsistentes(toAsistentesModelList(dto.getAsistentesId()));
    }

    

}
