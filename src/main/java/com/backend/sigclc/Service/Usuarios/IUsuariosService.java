package com.backend.sigclc.Service.Usuarios;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Estadisticas.LectorActivoResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioCreateDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioUpdateDTO;

public interface IUsuariosService {
    public UsuarioResponseDTO guardarUsuario(UsuarioCreateDTO usuario);
    public List<UsuarioResponseDTO> listarUsuarios();
    public UsuarioResponseDTO  buscarUsuariosPorId(ObjectId id);
    public UsuarioResponseDTO  actualizarUsuario(ObjectId id, UsuarioUpdateDTO usuario);
    public void sincronizarNombreUsuario(ObjectId id, String nombreCompleto);
    public String eliminarUsuario(ObjectId id);
    public List<LectorActivoResponseDTO> lectoresMasActivosMensual();
}
