package com.backend.sigclc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;
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
}
