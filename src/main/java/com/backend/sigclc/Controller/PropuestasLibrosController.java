package com.backend.sigclc.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.VotoDTO;
import com.backend.sigclc.Service.PropuestasLibros.IPropuestasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SIGCLC/backend/propuestas")
@CrossOrigin(origins = "*")
public class PropuestasLibrosController {
    @Autowired
    private IPropuestasService propuestasService;

    @PostMapping(value = "/insertar")
    public ResponseEntity<PropuestaLibroResponseDTO> crearLibro(@Valid @RequestBody PropuestaLibroCreateDTO dto) {
        PropuestaLibroResponseDTO response = propuestasService.guardarPropuesta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/votar/{id}")
    public ResponseEntity<PropuestaLibroResponseDTO> votarPropuesta(@PathVariable ObjectId id, @Valid @RequestBody VotoDTO dto) {
        PropuestaLibroResponseDTO response = propuestasService.votarPropuesta(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
