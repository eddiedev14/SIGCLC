package com.backend.sigclc.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.sigclc.DTO.Usuarios.UsuarioCreateDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioUpdateDTO;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Service.Usuarios.IUsuariosService;

@RestController
@RequestMapping("/SIGCLC/backend") // endpoint base
@CrossOrigin(origins = "*") // permite peticiones desde Postman o frontend
public class UsuariosController {

    @Autowired 
    private IUsuariosService usuariosService;

    // 🔹 Crear usuario
    @PostMapping("/insertar")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody UsuarioCreateDTO usuario) {
        return new ResponseEntity<>(usuariosService.guardarUsuario(usuario), HttpStatus.CREATED);
    }

    // 🔹 Listar todos los usuarios
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return new ResponseEntity<>(usuariosService.listarUsuarios(), HttpStatus.OK);
    }

    // 🔹 Buscar usuario por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuariosModel> buscarUsuarioPorId(@PathVariable ObjectId id) {
        return new ResponseEntity<>(usuariosService.buscarUsuariosPorId(id), HttpStatus.OK);
    }

    // 🔹 Actualizar usuario
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuariosModel> actualizarUsuario(
            @PathVariable ObjectId id,
            @RequestBody UsuarioUpdateDTO usuario) {
        return new ResponseEntity<>(usuariosService.actualizarUsuario(id, usuario), HttpStatus.OK);
    }

    // 🔹 Eliminar usuario
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable ObjectId id) {
        return new ResponseEntity<>(usuariosService.eliminarUsuario(id), HttpStatus.OK);
    }
}
