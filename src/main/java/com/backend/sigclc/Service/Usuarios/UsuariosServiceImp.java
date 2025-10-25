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
import com.backend.sigclc.Repository.IUsuariosRepository;

@Service
public class UsuariosServiceImp implements IUsuariosService {

    @Autowired 
    private IUsuariosRepository usuariosRepository;

    @Autowired 
    private UsuarioMapper usuarioMapper;

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

        // Convertimos a DTO de respuesta y lo devolvemos
        return usuarioMapper.toResponseDTO(actualizado);
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
