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
    public UsuariosModel buscarUsuariosPorId(ObjectId id) {
        return usuariosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + id + " o está mal escrito."));
    }

    @Override
    public UsuariosModel actualizarUsuario(ObjectId id, UsuarioUpdateDTO dto) {
        UsuariosModel existente = usuariosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario con id: " + id));

        // Solo actualizamos los campos no nulos del DTO
        if (dto.getNombreCompleto() != null) existente.setNombreCompleto(dto.getNombreCompleto());
        if (dto.getEdad() != null) existente.setEdad(dto.getEdad());
        if (dto.getOcupacion() != null) existente.setOcupacion(dto.getOcupacion());
        if (dto.getCorreoElectronico() != null) existente.setCorreoElectronico(dto.getCorreoElectronico());
        if (dto.getTelefono() != null) existente.setTelefono(dto.getTelefono());

        return usuariosRepository.save(existente);
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
