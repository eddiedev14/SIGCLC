package com.backend.sigclc.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaUpdateDTO;
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

    @PatchMapping(value = "/actualizar/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ReseniaResponseDTO> actualizarResenia(@PathVariable ObjectId id, @Valid @ModelAttribute ReseniaUpdateDTO dto) {
        ReseniaResponseDTO response = reseniasService.actualizarResenia(id, dto);
        return ResponseEntity.ok(response);
    }
}
