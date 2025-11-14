package com.backend.sigclc.Service.Usuarios;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.sigclc.DTO.Usuarios.UsuarioCreateDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioUpdateDTO;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.UsuarioMapper;
import com.backend.sigclc.Repository.ILibrosRepository;
import com.backend.sigclc.Repository.IPropuestasLibrosRepository;
import com.backend.sigclc.Repository.IReunionesRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;
import com.backend.sigclc.Repository.IForosRepository;

@Service
public class UsuariosServiceImp implements IUsuariosService {

    @Autowired
    private IForosRepository forosRepository;

    @Autowired 
    private IUsuariosRepository usuariosRepository;

    @Autowired 
    private ILibrosRepository librosRepository;

    @Autowired 
    private IPropuestasLibrosRepository propuestasLibrosRepository;

    @Autowired 
    private UsuarioMapper usuarioMapper;

    @Autowired
    private IReunionesRepository reunionesRepository;

    @Override
    public UsuarioResponseDTO guardarUsuario(UsuarioCreateDTO usuario) {
        UsuariosModel model = usuarioMapper.toModel(usuario);
        usuariosRepository.save(model);
        return usuarioMapper.toResponseDTO(model);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        List<UsuariosModel> usuarios = usuariosRepository.findAll();
        return usuarioMapper.toResponseDTOList(usuarios);
    }

    @Override
    public UsuarioResponseDTO buscarUsuariosPorId(ObjectId id) {
        UsuariosModel usuario = usuariosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + id + " o está mal escrito."));

        // Aquí usamos el mapper en lugar del método manual
        return usuarioMapper.toResponseDTO(usuario);
}


    @Override
    public UsuarioResponseDTO actualizarUsuario(ObjectId id, UsuarioUpdateDTO dto) {
        // Buscar el usuario existente
        UsuariosModel existente = usuariosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario con id: " + id));

        // Usamos el mapper para actualizar solo los campos no nulos
        usuarioMapper.updateModelFromDTO(dto, existente);

        // Guardamos el usuario actualizado
        UsuariosModel actualizado = usuariosRepository.save(existente);

        // Propagar cambio de nombre en caso de que haya sido modificado a otras colecciones
        if (dto.getNombreCompleto() != null) {
            sincronizarNombreUsuario(id, dto.getNombreCompleto());
        }

        // Convertimos a DTO de respuesta y lo devolvemos
        return usuarioMapper.toResponseDTO(actualizado);
    }

    @Override
    public void sincronizarNombreUsuario(ObjectId usuarioId, String nombreCompleto) {
        librosRepository.actualizarNombreCreador(usuarioId, nombreCompleto);
        propuestasLibrosRepository.actualizarNombreUsuarioProponente(usuarioId, nombreCompleto);
        propuestasLibrosRepository.actualizarNombreUsuarioVoto(usuarioId, nombreCompleto);
        reunionesRepository.actualizarNombreAsistente(usuarioId, nombreCompleto);
        forosRepository.actualizarNombreModerador(usuarioId, nombreCompleto);
    }

    @Override
    public String eliminarUsuario(ObjectId id) {
        if (!usuariosRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No se encontró el usuario con id: " + id);
        }
        usuariosRepository.deleteById(id);
        return "Usuario eliminado correctamente con id: " + id;
    }
}
