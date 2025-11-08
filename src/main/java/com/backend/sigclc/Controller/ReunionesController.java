package com.backend.sigclc.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionUpdateDTO;
import com.backend.sigclc.Service.Reuniones.IReunionesService;

@RestController
@RequestMapping("/SIGCLC/backend/reuniones")
@CrossOrigin(origins = "*")
public class ReunionesController {
    @Autowired
    private IReunionesService reunionesService;

    @PostMapping(value = "/insertar", consumes = "multipart/form-data")
    public ResponseEntity<ReunionResponseDTO> insertarReunion(@ModelAttribute ReunionCreateDTO dto) {
        ReunionResponseDTO respuesta = reunionesService.guardarReunion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<ReunionResponseDTO>> listarReuniones() {
        List<ReunionResponseDTO> response = reunionesService.listarReuniones();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(value = "/actualizar/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ReunionResponseDTO> actualizarReunion(@PathVariable org.bson.types.ObjectId id, @ModelAttribute ReunionUpdateDTO dto) {
        ReunionResponseDTO respuesta = reunionesService.actualizarReunion(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    @DeleteMapping(value = "/eliminar/{id}")
    public ResponseEntity<String> eliminarReunion(@org.springframework.web.bind.annotation.PathVariable org.bson.types.ObjectId id) {
        String respuesta = reunionesService.eliminarReunion(id);
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
}
