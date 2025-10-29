package com.backend.sigclc.Service.Libros;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;

public interface ILibrosService {
    public LibroResponseDTO guardarLibro(LibroCreateDTO libro);
    public List<LibroResponseDTO> listarLibros();
    public LibroResponseDTO buscarLibrosPorId(ObjectId id);
    public LibroResponseDTO actualizarLibro(String id, LibroUpdateDTO libro);
    public String eliminarLibro(String id);
}
