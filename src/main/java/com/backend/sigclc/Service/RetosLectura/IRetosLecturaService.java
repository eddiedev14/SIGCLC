package com.backend.sigclc.Service.RetosLectura;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.RetosLectura.RetoLecturaCreateDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaUpdateDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoDTO;

public interface IRetosLecturaService {
    public RetoLecturaResponseDTO crearRetoLectura(RetoLecturaCreateDTO retoLectura);
    public RetoLecturaResponseDTO registrarProgreso(ObjectId retoId, ObjectId usuarioId, ProgresoDTO progreso);
    public String eliminarRetoLectura(ObjectId retoId);
    public RetoLecturaResponseDTO actualizarReto(ObjectId retoId, RetoLecturaUpdateDTO dto);
    public RetoLecturaResponseDTO agregarUsuarios(ObjectId retoId, List<ObjectId> usuariosIds);
    public RetoLecturaResponseDTO eliminarUsuarios(ObjectId retoId, List<ObjectId> usuariosIds);
    public RetoLecturaResponseDTO agregarLibros(ObjectId retoId, List<ObjectId> librosIds);
    public RetoLecturaResponseDTO eliminarLibros(ObjectId retoId, List<ObjectId> librosIds);
    public List<RetoLecturaResponseDTO> listarRetosLectura();
    public RetoLecturaResponseDTO buscarRetoLectura(ObjectId retoId);
    public List<RetoLecturaResponseDTO> buscarRetosPorUsuario(ObjectId usuarioId);
    public List<RetoLecturaResponseDTO> buscarRetosPorLibro(ObjectId libroId);
    public List<RetoLecturaResponseDTO> buscarRetosActivos();
}
