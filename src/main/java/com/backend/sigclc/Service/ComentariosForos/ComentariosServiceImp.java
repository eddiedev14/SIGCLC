package com.backend.sigclc.Service.ComentariosForos;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoCreateDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoResponseDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoUpdateDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.ComentarioForoMapper;
import com.backend.sigclc.Model.ComentariosForos.ComentarioForoModel;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Repository.IComentariosForosRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;

@Service
public class ComentariosServiceImp implements IComentariosService {

    @Autowired
    private IComentariosForosRepository comentariosRepository;

    @Autowired
    private ComentarioForoMapper comentarioForoMapper;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Autowired
    private IComentariosForosRepository comentarioForoRepository;

    @Override
    public ComentarioForoResponseDTO guardarComentario(ComentarioForoCreateDTO comentarioDTO) {
        // DTO → Model
        ComentarioForoModel model = comentarioForoMapper.toModel(comentarioDTO);

        // fecha actual
        model.setFechaPublicacion(new Date());

        // Obtener el ID del usuario desde el DTO
        ObjectId usuarioId = model.getRedactor().getUsuarioId();

        // Buscar el usuario en la colección 'usuarios'
        UsuariosModel redactor = usuariosRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + usuarioId));

        model.getRedactor().setNombreCompleto(redactor.getNombreCompleto());

        // guardar
        comentarioForoRepository.save(model);

        // respuesta
        return comentarioForoMapper.toResponseDTO(model);

    }

    @Override
    public List<ComentarioForoResponseDTO> listarComentariosPorForo(ObjectId foroId) {
        List<ComentarioForoModel> comentarios = comentariosRepository.buscarPorForoId(foroId);
        return comentarioForoMapper.toResponseDTOList(comentarios);
    }

    @Override
    public ComentarioForoResponseDTO buscarComentarioPorId(ObjectId id) {
        ComentarioForoModel comentario = comentariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con ID: " + id));
        return comentarioForoMapper.toResponseDTO(comentario);
    }

    @Override
    public List<ComentarioForoResponseDTO> listarComentariosPorParent(ObjectId parentId) {
        List<ComentarioForoModel> comentarios = comentariosRepository.buscarPorParentId(parentId);
        return comentarioForoMapper.toResponseDTOList(comentarios);
    }

    @Override
    public ComentarioForoResponseDTO actualizarComentario(ObjectId id, ComentarioForoUpdateDTO comentarioDTO) {
        ComentarioForoModel comentario = comentariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró comentario con ID: " + id));

        comentarioForoMapper.updateModelFromDTO(comentarioDTO, comentario);
        ComentarioForoModel actualizado = comentariosRepository.save(comentario);
        return comentarioForoMapper.toResponseDTO(actualizado);
    }

    @Override
    public String eliminarComentario(ObjectId id) {
        if (!comentariosRepository.existsById(id)) {
            throw new RuntimeException("No se encontró comentario con ID: " + id);
        }
        comentariosRepository.deleteById(id);
        return "Comentario eliminado correctamente con ID: " + id;
    }
}