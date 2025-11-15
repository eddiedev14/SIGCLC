package com.backend.sigclc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;
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
}
