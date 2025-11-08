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

import com.backend.sigclc.DTO.Foros.ForoCreateDTO;
import com.backend.sigclc.DTO.Foros.ForoResponseDTO;
import com.backend.sigclc.DTO.Foros.ForoUpdateDTO;
import com.backend.sigclc.Model.Foros.TipoTematica;
import com.backend.sigclc.Service.Foros.IForosService;

@RestController
@RequestMapping("/SIGCLC/backend/foros")
@CrossOrigin(origins = "*")
public class ForosController {

    @Autowired
    private IForosService forosService;

    @PostMapping("/insertar")
    public ResponseEntity<ForoResponseDTO> crearForo(@RequestBody ForoCreateDTO foro) {
        return ResponseEntity.ok(forosService.guardarForo(foro));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ForoResponseDTO>> listarForos() {
        return ResponseEntity.ok(forosService.listarForos());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ForoResponseDTO> obtenerPorId(@PathVariable ObjectId id) {
        return ResponseEntity.ok(forosService.buscarForoPorId(id));
    }

    @GetMapping("/listar-por-nombre/{nombreTematica}")
    public ResponseEntity<ForoResponseDTO> obtenerPorNombreTematica(@PathVariable String nombreTematica) {
        return ResponseEntity.ok(forosService.listarPorNombreTematica(nombreTematica));
    }

    @GetMapping("/listar-por-tipo/{tipoTematica}")
    public ResponseEntity<List<ForoResponseDTO>> obtenerPorTipoTematica(@PathVariable TipoTematica tipoTematica) {
        return ResponseEntity.ok(forosService.listarPorTipoTematica(tipoTematica));
    }

    @GetMapping("/listar-por-moderador/{moderadorId}")
    public ResponseEntity<List<ForoResponseDTO>> obtenerPorModerador(@PathVariable ObjectId moderadorId) {
        return ResponseEntity.ok(forosService.listarPorModerador(moderadorId));
    }

    @PatchMapping("/actualizar/{id}")
    public ResponseEntity<ForoResponseDTO> actualizarForo(@PathVariable ObjectId id,
                                                          @RequestBody ForoUpdateDTO foro) {
        return ResponseEntity.ok(forosService.actualizarForo(id, foro));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarForo(@PathVariable ObjectId id) {
        return ResponseEntity.ok(forosService.eliminarForo(id));
    }
}
