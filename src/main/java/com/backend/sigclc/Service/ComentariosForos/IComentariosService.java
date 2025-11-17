package com.backend.sigclc.Service.ComentariosForos;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoCreateDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoResponseDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoUpdateDTO;

public interface IComentariosService {
    public ComentarioForoResponseDTO guardarComentario(ComentarioForoCreateDTO comentario);
    public List<ComentarioForoResponseDTO> listarComentariosPorForo(ObjectId foroId);
    public ComentarioForoResponseDTO buscarComentarioPorId(ObjectId id);
    public List<ComentarioForoResponseDTO> listarComentariosPorParent(ObjectId parentId);
    public ComentarioForoResponseDTO actualizarComentario(ObjectId id, ComentarioForoUpdateDTO comentario);
    public void sincronizarNombreUsuario(ObjectId id, String nombreCompleto);
    public List<ComentarioForoResponseDTO> listarComentariosRaizPorForo(ObjectId foroId);
    public List<ComentarioForoResponseDTO> listarComentariosPorUsuario(ObjectId usuarioId);
    public ComentarioForoResponseDTO buscarComentarioPadre(ObjectId comentarioId);
    public String eliminarComentario(ObjectId id);
}
