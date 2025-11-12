package com.backend.sigclc.Controller;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionUpdateDTO;
import com.backend.sigclc.Service.Reuniones.IReunionesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SIGCLC/backend/reuniones")
@CrossOrigin(origins = "*")
public class ReunionesController {
    @Autowired
    private IReunionesService reunionesService;

    @PostMapping(value = "/insertar", consumes = "multipart/form-data")
    public ResponseEntity<ReunionResponseDTO> insertarReunion(@Valid @ModelAttribute ReunionCreateDTO dto) {
        ReunionResponseDTO response = reunionesService.guardarReunion(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<ReunionResponseDTO>> listarReuniones() {
        List<ReunionResponseDTO> response = reunionesService.listarReuniones();
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/actualizar/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ReunionResponseDTO> actualizarReunion(@PathVariable ObjectId id, @Valid @ModelAttribute ReunionUpdateDTO dto) {
        ReunionResponseDTO response = reunionesService.actualizarReunion(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/agregar-libros/{id}")
    public ResponseEntity<ReunionResponseDTO> agregarLibrosAReunion(@PathVariable ObjectId id, @RequestBody List<ObjectId> librosSeleccionadosId) {
        ReunionResponseDTO response = reunionesService.agregarLibrosAReunion(id, librosSeleccionadosId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/agregar-asistentes/{reunionId}")
    public ResponseEntity<ReunionResponseDTO> agregarAsistentesAReunion(@PathVariable ObjectId reunionId, @RequestBody List<ObjectId> asistentesIds) {
        ReunionResponseDTO response = reunionesService.agregarAsistentesAReunion(reunionId, asistentesIds);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/eliminar-libros/{id}")
    public ResponseEntity<ReunionResponseDTO> eliminarLibrosDeReunion(
            @PathVariable ObjectId id,
            @RequestBody List<ObjectId> librosSeleccionadosId) {

        ReunionResponseDTO response = reunionesService.eliminarLibrosSeleccionadosDeReunion(id, librosSeleccionadosId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/eliminar-asistentes/{reunionId}")
    public ResponseEntity<ReunionResponseDTO> eliminarAsistentesDeReunion(
            @PathVariable ObjectId reunionId,
            @RequestBody List<ObjectId> asistentesIds) {

        ReunionResponseDTO response = reunionesService.eliminarAsistentesDeReunion(reunionId, asistentesIds);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/agregar-archivos/{reunionId}", consumes = "multipart/form-data")
    public ResponseEntity<ReunionResponseDTO> agregarArchivosAReunion(
            @PathVariable ObjectId reunionId,
            @ModelAttribute List<MultipartFile> archivosAdjuntos) {

        ReunionResponseDTO response = reunionesService.agregarArchivosAReunion(reunionId, archivosAdjuntos);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping(value = "/eliminar/{id}")
    public ResponseEntity<String> eliminarReunion(@PathVariable ObjectId id) {
        String response = reunionesService.eliminarReunion(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listar-por-asistente/{asistenteId}")
    public ResponseEntity<List<ReunionResponseDTO>> listarPorAsistenteId(@PathVariable ObjectId asistenteId) {
        List<ReunionResponseDTO> response = reunionesService.listarPorAsistenteId(asistenteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listar-por-libro/{libroId}")
    public ResponseEntity<List<ReunionResponseDTO>> listarPorLibroSeleccionadoId(@PathVariable ObjectId libroId) {
        List<ReunionResponseDTO> response = reunionesService.listarPorLibroSeleccionadoId(libroId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listar-por-fecha/{fecha}")
    public ResponseEntity<List<ReunionResponseDTO>> listarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha) {
        List<ReunionResponseDTO> response = reunionesService.listarPorFecha(fecha);
        return ResponseEntity.ok(response);
    }
}