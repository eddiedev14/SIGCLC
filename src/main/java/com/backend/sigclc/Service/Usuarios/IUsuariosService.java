package com.backend.sigclc.Service.Usuarios;

import java.util.List;

import org.bson.types.ObjectId;
import com.backend.sigclc.DTO.Usuarios.UsuarioCreateDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioUpdateDTO;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;

public interface IUsuariosService {
    public UsuarioResponseDTO guardarUsuario(UsuarioCreateDTO empleado);
    public List<UsuarioResponseDTO> listarUsuarios();
    public UsuariosModel buscarUsuariosPorId(ObjectId id);
    public UsuariosModel actualizarUsuario(ObjectId id, UsuarioUpdateDTO usuario);
    public String eliminarUsuario(ObjectId id);
}
