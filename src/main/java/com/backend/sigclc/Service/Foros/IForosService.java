package com.backend.sigclc.Service.Foros;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Foros.ForoCreateDTO;
import com.backend.sigclc.DTO.Foros.ForoResponseDTO;
import com.backend.sigclc.DTO.Foros.ForoUpdateDTO;
import com.backend.sigclc.Model.Foros.TipoTematica;

public interface IForosService {
    public ForoResponseDTO guardarForo(ForoCreateDTO foro);
    public List<ForoResponseDTO> listarForos();
    public ForoResponseDTO buscarForoPorId(ObjectId id);
    public ForoResponseDTO listarPorTitulo(String titulo);
    public ForoResponseDTO listarPorTematica(String tematica);
    public List<ForoResponseDTO> listarPorTipoTematica(TipoTematica tipoTematica);
    public List<ForoResponseDTO> listarPorModerador(ObjectId moderadorId);
    public ForoResponseDTO actualizarForo(ObjectId id, ForoUpdateDTO foro);
    public String eliminarForo(ObjectId id);
}
