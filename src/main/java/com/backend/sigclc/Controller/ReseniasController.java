package com.backend.sigclc.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioCreateDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioResponseDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionCreateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionResponseDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionUpdateDTO;
import com.backend.sigclc.Service.Resenias.IReseniasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SIGCLC/backend/resenias")
@CrossOrigin(origins = "*")
public class ReseniasController {
    @Autowired
    private IReseniasService reseniasService;
    
    @PostMapping(value = "/insertar", consumes = "multipart/form-data")
    public ResponseEntity<ReseniaResponseDTO> insertarResenia(@Valid @ModelAttribute ReseniaCreateDTO dto) {
        ReseniaResponseDTO response = reseniasService.crearResenia(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<ReseniaResponseDTO>> listarResenias() {
        List<ReseniaResponseDTO> response = reseniasService.listarResenias();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/buscar/{id}")
    public ResponseEntity<ReseniaResponseDTO> buscarReseniaPorId(@PathVariable ObjectId id) {
        ReseniaResponseDTO response = reseniasService.buscarReseniaPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/buscarPorRedactor/{redactorId}")
    public ResponseEntity<List<ReseniaResponseDTO>> buscarReseniasPorRedactor(@PathVariable ObjectId redactorId) {
        List<ReseniaResponseDTO> response = reseniasService.buscarReseniasPorRedactor(redactorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/buscarPorLibro/{libroId}")
    public ResponseEntity<List<ReseniaResponseDTO>> buscarReseniasPorLibro(@PathVariable ObjectId libroId) {
        List<ReseniaResponseDTO> response = reseniasService.buscarReseniasPorLibro(libroId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/buscarComentariosDeUsuario/{usuarioId}")
    public ResponseEntity<List<ComentarioResponseDTO>> buscarComentariosDeUsuario(@PathVariable ObjectId usuarioId) {
        List<ComentarioResponseDTO> response = reseniasService.buscarComentariosDeUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/buscarValoracionesDeUsuario/{usuarioId}")
    public ResponseEntity<List<ValoracionResponseDTO>> buscarValoracionesDeUsuario(@PathVariable ObjectId usuarioId) {
        List<ValoracionResponseDTO> response = reseniasService.buscarValoracionesDeUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/actualizar/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ReseniaResponseDTO> actualizarResenia(@PathVariable ObjectId id, @Valid @ModelAttribute ReseniaUpdateDTO dto) {
        ReseniaResponseDTO response = reseniasService.actualizarResenia(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/valorar/{id}")
    public ResponseEntity<ReseniaResponseDTO> valorarResenia(@PathVariable ObjectId id, @Valid @RequestBody ValoracionCreateDTO dto) {
        ReseniaResponseDTO response = reseniasService.valorarResenia(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/comentar/{id}")
    public ResponseEntity<ReseniaResponseDTO> comentarResenia(@PathVariable ObjectId id, @Valid @RequestBody ComentarioCreateDTO dto) {
        ReseniaResponseDTO response = reseniasService.comentarResenia(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/agregar-archivos/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ReseniaResponseDTO> agregarArchivosAResenia(@PathVariable ObjectId id, @ModelAttribute List<MultipartFile> archivosAdjuntos) {
        ReseniaResponseDTO response = reseniasService.agregarArchivosAResenia(id, archivosAdjuntos);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/actualizar-valoracion/{idResenia}/{idUsuario}")
    public ResponseEntity<ReseniaResponseDTO> actualizarValoracion(@PathVariable ObjectId idResenia, @PathVariable ObjectId idUsuario, @Valid @RequestBody ValoracionUpdateDTO dto) {
        ReseniaResponseDTO response = reseniasService.actualizarValoracion(idResenia, idUsuario, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/actualizar-comentario/{idResenia}/{idComentario}")
    public ResponseEntity<ReseniaResponseDTO> actualizarComentario(@PathVariable ObjectId idResenia, @PathVariable ObjectId idComentario, @Valid @RequestBody ComentarioUpdateDTO dto) {
        ReseniaResponseDTO response = reseniasService.actualizarComentario(idResenia, idComentario, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/eliminar-valoracion/{idResenia}/{idUsuario}")
    public ResponseEntity<ReseniaResponseDTO> eliminarValoracion(@PathVariable ObjectId idResenia, @PathVariable ObjectId idUsuario) {
        ReseniaResponseDTO response = reseniasService.eliminarValoracion(idResenia, idUsuario);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/eliminar-comentario/{idResenia}/{idComentario}")
    public ResponseEntity<ReseniaResponseDTO> eliminarComentario(@PathVariable ObjectId idResenia, @PathVariable ObjectId idComentario) {
        ReseniaResponseDTO response = reseniasService.eliminarComentario(idResenia, idComentario);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/eliminar-archivos/{id}")
    public ResponseEntity<ReseniaResponseDTO> eliminarArchivosDeResenia(@PathVariable ObjectId id, @RequestBody List<String> archivosAdjuntosUuid) {
        ReseniaResponseDTO response = reseniasService.eliminarArchivosDeResenia(id, archivosAdjuntosUuid);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/eliminar/{id}")
    public ResponseEntity<String> eliminarResenia(@PathVariable ObjectId id) {
        String response = reseniasService.eliminarResenia(id);
        return ResponseEntity.ok(response);
    }
}
