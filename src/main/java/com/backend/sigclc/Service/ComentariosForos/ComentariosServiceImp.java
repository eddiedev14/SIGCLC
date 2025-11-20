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
import com.backend.sigclc.Repository.IForosRepository;
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
    private IForosRepository forosRepository;

    @Override
    public ComentarioForoResponseDTO guardarComentario(ComentarioForoCreateDTO comentarioDTO) {
        // DTO → Model
        ComentarioForoModel model = comentarioForoMapper.toModel(comentarioDTO);

        // Validar que el foro exista
        ObjectId foroId = model.getForoId();
        forosRepository.findById(foroId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Error! No existe un foro con id: " + foroId));

        // Si viene parentId, validar que ese parentId comment exista y que adeams sea de este foro y no de otro
        if (comentarioDTO.getParentId() != null) {
            ObjectId parentId = comentarioDTO.getParentId();
            ComentarioForoModel parent = comentariosRepository.findById(parentId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Error! No existe un comentario con id: " + parentId));
            if (!parent.getForoId().equals(foroId)) {
                throw new RecursoNoEncontradoException(
                        "Error! El comentario con id: " + parentId + " no pertenece al foro con id: " + foroId);
            }
        }

        // fecha actual
        model.setFechaPublicacion(new Date());

        // Obtener el ID del usuario desde el modelo
        ObjectId usuarioId = model.getRedactor().getUsuarioId();

        // Buscar el usuario en la colección 'usuarios'
        UsuariosModel redactor = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Error! No existe un usuario con id: " + usuarioId));

        // completar datos del redactor
        model.getRedactor().setNombreCompleto(redactor.getNombreCompleto());

        // guardar
        ComentarioForoModel guardado = comentariosRepository.save(model);

        // respuesta
        return comentarioForoMapper.toResponseDTO(guardado);
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
    public void sincronizarNombreUsuario(ObjectId usuarioId, String nombreCompleto) {
        comentariosRepository.actualizarNombreRedactor(usuarioId, nombreCompleto);
    }

    @Override
    public String eliminarComentario(ObjectId id) {
        if (!comentariosRepository.existsById(id)) {
            throw new RuntimeException("No se encontró comentario con ID: " + id);
        }

        // Si se elimina un comentario se eliminan los comentarios hijos si tiene
        comentariosRepository.deleteAllByParentId(id);

        comentariosRepository.deleteById(id);
        return "Comentario eliminado correctamente con ID: " + id;
    }

    @Override
    public List<ComentarioForoResponseDTO> listarComentariosRaizPorForo(ObjectId foroId) {
        List<ComentarioForoModel> comentarios = comentariosRepository.buscarComentariosPadrePorForoId(foroId);
        return comentarioForoMapper.toResponseDTOList(comentarios);
    }

    @Override
    public List<ComentarioForoResponseDTO> listarComentariosPorUsuario(ObjectId usuarioId) {
        List<ComentarioForoModel> comentarios = comentariosRepository.buscarComentariosPorUsuario(usuarioId);
        return comentarioForoMapper.toResponseDTOList(comentarios);
    }

    @Override
    public ComentarioForoResponseDTO buscarComentarioPadre(ObjectId comentarioId) {
        // Primero comprobamos que el comentario hijo existe (opcional pero recomendable)
        ComentarioForoModel comentario = comentariosRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con ID: " + comentarioId));

        // Si no responde a nadie
        if (comentario.getParentId() == null) {
            throw new RuntimeException("El comentario con ID: " + comentarioId + " no responde a ningún otro comentario");
            // Alternativa: devolver null o un DTO vacío según tu diseño
        }

        // Buscar el padre con la agregación
        return comentariosRepository.buscarComentarioPadre(comentarioId)
                .map(comentarioForoMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el comentario padre para el comentario con ID: " + comentarioId));
    }
}