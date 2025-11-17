package com.backend.sigclc.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sigclc.DTO.RetosLectura.RetoLecturaCreateDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaUpdateDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoDTO;
import com.backend.sigclc.Service.RetosLectura.IRetosLecturaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SIGCLC/backend/retos")
@CrossOrigin("*")
public class RetosLecturaController {
    @Autowired
    private IRetosLecturaService retosLecturaService;

    @PostMapping(value = "/insertar")
    public ResponseEntity<RetoLecturaResponseDTO> insertarReto(@Valid @RequestBody RetoLecturaCreateDTO dto) {
        RetoLecturaResponseDTO response = retosLecturaService.crearRetoLectura(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<RetoLecturaResponseDTO>> listarRetosLectura() {
        List<RetoLecturaResponseDTO> response = retosLecturaService.listarRetosLectura();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/buscar-por-id/{retoId}")
    public ResponseEntity<RetoLecturaResponseDTO> buscarRetosLectura(@PathVariable ObjectId retoId) {
        RetoLecturaResponseDTO response = retosLecturaService.buscarRetoLectura(retoId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/registrar-progreso/{retoId}/{usuarioId}")
    public ResponseEntity<RetoLecturaResponseDTO> registrarProgreso(
        @PathVariable ObjectId retoId, 
        @PathVariable ObjectId usuarioId, 
        @RequestBody ProgresoDTO dto) {
            RetoLecturaResponseDTO response = retosLecturaService.registrarProgreso(retoId, usuarioId, dto);
            return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/eliminar/{retoId}")
    public ResponseEntity<String> eliminarRetoLectura(@PathVariable ObjectId retoId) {
        String response = retosLecturaService.eliminarRetoLectura(retoId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/actualizar/{retoId}")
    public ResponseEntity<RetoLecturaResponseDTO> actualizarRetoLectura(
        @PathVariable ObjectId retoId,
        @Valid @RequestBody RetoLecturaUpdateDTO dto) {
            RetoLecturaResponseDTO response = retosLecturaService.actualizarReto(retoId, dto);
            return ResponseEntity.ok(response);
        }

    @PatchMapping("/agregar-usuarios/{retoId}")
    public ResponseEntity<RetoLecturaResponseDTO> agregarUsuarios(
            @PathVariable ObjectId retoId,
            @RequestBody List<ObjectId> usuariosIds) {

        RetoLecturaResponseDTO response = retosLecturaService.agregarUsuarios(retoId, usuariosIds);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/eliminar-usuarios/{retoId}")
    public ResponseEntity<RetoLecturaResponseDTO> eliminarUsuarios(
            @PathVariable ObjectId retoId,
            @RequestBody List<ObjectId> usuariosIds) {

        RetoLecturaResponseDTO response = retosLecturaService.eliminarUsuarios(retoId, usuariosIds);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/agregar-libros/{retoId}")
    public ResponseEntity<RetoLecturaResponseDTO> agregarLibros(
            @PathVariable ObjectId retoId,
            @RequestBody List<ObjectId> librosIds) {

        RetoLecturaResponseDTO response = retosLecturaService.agregarLibros(retoId, librosIds);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/eliminar-libros/{retoId}")
    public ResponseEntity<RetoLecturaResponseDTO> eliminarLibros(
            @PathVariable ObjectId retoId,
            @RequestBody List<ObjectId> librosIds) {
        RetoLecturaResponseDTO response = retosLecturaService.eliminarLibros(retoId, librosIds);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar-por-usuario/{usuarioId}")
    public ResponseEntity<List<RetoLecturaResponseDTO>> listarRetosPorUsuario(@PathVariable ObjectId usuarioId) {
        List<RetoLecturaResponseDTO> response = retosLecturaService.buscarRetosPorUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar-por-libro/{libroId}")
    public ResponseEntity<List<RetoLecturaResponseDTO>> listarRetosPorLibro(@PathVariable ObjectId libroId) {
        List<RetoLecturaResponseDTO> response = retosLecturaService.buscarRetosPorLibro(libroId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar-activos")
    public ResponseEntity<List<RetoLecturaResponseDTO>> listarRetosActivos() {
        List<RetoLecturaResponseDTO> response = retosLecturaService.buscarRetosActivos();
        return ResponseEntity.ok(response);
    }
}
