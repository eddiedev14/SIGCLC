package com.backend.sigclc.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sigclc.DTO.Usuarios.UsuarioCreateDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioUpdateDTO;
import com.backend.sigclc.Service.Usuarios.IUsuariosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SIGCLC/backend/usuarios") // endpoint base
@CrossOrigin(origins = "*") // permite peticiones desde Postman o frontend
public class UsuariosController {

    @Autowired 
    private IUsuariosService usuariosService;

    // Crear usuario
    @PostMapping("/insertar")
        public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO response = usuariosService.guardarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Listar todos los usuarios
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuariosService.listarUsuarios());
    }

    // Buscar usuario por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable ObjectId id) {
        return ResponseEntity.ok(usuariosService.buscarUsuariosPorId(id));
    }

    // Actualizar usuario
    @PatchMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable ObjectId id, @Valid @RequestBody UsuarioUpdateDTO usuario) {
        UsuarioResponseDTO actualizado = usuariosService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar usuario
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable ObjectId id) {
        return ResponseEntity.ok(usuariosService.eliminarUsuario(id));
    }
}
