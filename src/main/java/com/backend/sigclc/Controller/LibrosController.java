package com.backend.sigclc.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;
import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Service.Libros.ILibrosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SIGCLC/backend/libros")
@CrossOrigin(origins = "*")
public class LibrosController {
    
    @Autowired
    private ILibrosService librosService;

    @PostMapping(value = "/insertar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LibroResponseDTO> crearLibro(@Valid @RequestBody LibroCreateDTO dto) {
        LibroResponseDTO response = librosService.guardarLibro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<LibroResponseDTO>> listarLibros() {
        return ResponseEntity.ok(librosService.listarLibros());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<LibroResponseDTO> buscarLibroPorId(@PathVariable ObjectId id) {
        return ResponseEntity.ok(librosService.buscarLibroPorId(id));
    }
    
    @GetMapping("/listar-por-genero/{genero}")
    public ResponseEntity<List<LibroResponseDTO>> listarLibroPorGenero(@PathVariable GeneroLibro genero) {
        return ResponseEntity.ok(librosService.listarPorGenero(genero));
    }

    @GetMapping("/listar-por-autor/{autor}")
    public ResponseEntity<List<LibroResponseDTO>> listarLibroPorAutor(@PathVariable String autor) {
        return ResponseEntity.ok(librosService.listarPorAutor(autor));
    }

    @PatchMapping(value = "/actualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LibroResponseDTO> actualizarLibro(
            @PathVariable ObjectId id,
            @Valid @RequestBody LibroUpdateDTO libro) {

        LibroResponseDTO actualizado = librosService.actualizarLibro(id, libro);
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarLibro(@PathVariable ObjectId id) {
        return ResponseEntity.ok(librosService.eliminarLibro(id));
    }
}
