package com.backend.sigclc.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroUpdateDTO;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;
import com.backend.sigclc.Service.PropuestasLibros.IPropuestasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SIGCLC/backend/propuestas")
@CrossOrigin(origins = "*")
public class PropuestasLibrosController {
    @Autowired
    private IPropuestasService propuestasService;

    @PostMapping(value = "/insertar")
    public ResponseEntity<PropuestaLibroResponseDTO> crearPropuesta(@Valid @RequestBody PropuestaLibroCreateDTO dto) {
        PropuestaLibroResponseDTO response = propuestasService.guardarPropuesta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/votar/{idPropuesta}/{idUsuario}")
    public ResponseEntity<PropuestaLibroResponseDTO> votarPropuesta(@PathVariable ObjectId idPropuesta, @PathVariable ObjectId idUsuario) {
        PropuestaLibroResponseDTO response = propuestasService.votarPropuesta(idPropuesta, idUsuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<PropuestaLibroResponseDTO>> listarPropuestas() {
        List<PropuestaLibroResponseDTO> response = propuestasService.listarPropuestas();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/buscar/{id}")
    public ResponseEntity<PropuestaLibroResponseDTO> buscarPropuesta(@PathVariable ObjectId id) {
        PropuestaLibroResponseDTO response = propuestasService.buscarPropuesta(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listarPorEstado/{estado}")
    public ResponseEntity<List<PropuestaLibroResponseDTO>> listarPropuestasPorEstado(@PathVariable EstadoPropuesta estado) {
        List<PropuestaLibroResponseDTO> response = propuestasService.listarPropuestasPorEstado(estado);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listarPorUsuario/{usuarioId}")
    public ResponseEntity<List<PropuestaLibroResponseDTO>> listarPropuestasPorUsuario(@PathVariable ObjectId usuarioId) {
        List<PropuestaLibroResponseDTO> response = propuestasService.listarPropuestasPorUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listarPorLibro/{libroId}")
    public ResponseEntity<List<PropuestaLibroResponseDTO>> listarPropuestasPorLibro(@PathVariable ObjectId libroId) {
        List<PropuestaLibroResponseDTO> response = propuestasService.listarPropuestasPorLibro(libroId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/actualizar/{id}")
    public ResponseEntity<PropuestaLibroResponseDTO> actualizarPropuesta(@PathVariable ObjectId id, @Valid @RequestBody PropuestaLibroUpdateDTO dto) {
        PropuestaLibroResponseDTO response = propuestasService.actualizarPropuesta(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/eliminar/{id}")
    public ResponseEntity<String> eliminarPropuesta(@PathVariable ObjectId id) {
        String response = propuestasService.eliminarPropuesta(id);
        return ResponseEntity.ok(response);
    }
}
