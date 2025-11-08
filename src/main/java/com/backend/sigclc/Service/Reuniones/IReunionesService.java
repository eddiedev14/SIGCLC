package com.backend.sigclc.Service.Reuniones;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;

public interface IReunionesService {
    public ReunionResponseDTO guardarReunion(ReunionCreateDTO reunion);
    public List<ReunionResponseDTO> listarReuniones();
    public String eliminarReunion(ObjectId id);
}
