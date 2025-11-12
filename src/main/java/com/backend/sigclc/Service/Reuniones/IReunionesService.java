package com.backend.sigclc.Service.Reuniones;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionUpdateDTO;

public interface IReunionesService {
    public ReunionResponseDTO guardarReunion(ReunionCreateDTO reunion);
    public List<ReunionResponseDTO> listarReuniones();
    public String eliminarReunion(ObjectId id);
    public ReunionResponseDTO actualizarReunion(ObjectId id, ReunionUpdateDTO dto);
    public ReunionResponseDTO agregarLibrosAReunion(ObjectId reunionId, List<String> librosIds);
    public ReunionResponseDTO agregarAsistentesAReunion(ObjectId reunionId, List<String> asistentesIds);
    public ReunionResponseDTO eliminarAsistentesDeReunion(ObjectId id, List<String> asistentesId);
    public ReunionResponseDTO eliminarLibrosSeleccionadosDeReunion(ObjectId id, List<String> librosId);
    public ReunionResponseDTO agregarArchivosAReunion(ObjectId id, List<MultipartFile> archivosAdjuntos);
    public List<ReunionResponseDTO> listarPorAsistenteId(ObjectId asistenteId);
    public List<ReunionResponseDTO> listarPorLibroSeleccionadoId(ObjectId libroId);
    public List<ReunionResponseDTO> listarPorFecha(Date fecha);
}
