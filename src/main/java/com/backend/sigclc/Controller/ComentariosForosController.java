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

import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoCreateDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoResponseDTO;
import com.backend.sigclc.DTO.ComentariosForos.ComentarioForoUpdateDTO;
import com.backend.sigclc.Service.ComentariosForos.IComentariosService;

@RestController
@RequestMapping("/SIGCLC/backend/comentariosforos")
@CrossOrigin(origins = "*")
public class ComentariosForosController {

    @Autowired
    private IComentariosService comentariosService;

    @PostMapping("/insertar")
    public ResponseEntity<ComentarioForoResponseDTO> crearComentario(
            @RequestBody ComentarioForoCreateDTO comentario) {
        return ResponseEntity.ok(comentariosService.guardarComentario(comentario));
    }

    @GetMapping("/listar-por-foro/{foroId}")
    public ResponseEntity<List<ComentarioForoResponseDTO>> listarComentariosPorForo(
            @PathVariable ObjectId foroId) {
        return ResponseEntity.ok(comentariosService.listarComentariosPorForo(foroId));
    }

    @GetMapping("/listar-por-comentariopadre/{parentId}")
    public ResponseEntity<List<ComentarioForoResponseDTO>> listarComentariosPorParent(
            @PathVariable ObjectId parentId) {
        return ResponseEntity.ok(comentariosService.listarComentariosPorParent(parentId));
    }

    @PatchMapping("/actualizar/{id}")
    public ResponseEntity<ComentarioForoResponseDTO> actualizarComentario(
            @PathVariable ObjectId id,
            @RequestBody ComentarioForoUpdateDTO comentarioUpdateDTO) {
        return ResponseEntity.ok(comentariosService.actualizarComentario(id, comentarioUpdateDTO));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarComentario(@PathVariable ObjectId id) {
        return ResponseEntity.ok(comentariosService.eliminarComentario(id));
    }
}