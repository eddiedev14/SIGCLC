package com.backend.sigclc.Service.Libros;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;
import com.backend.sigclc.Model.Libros.GeneroLibro;

public interface ILibrosService {
    public LibroResponseDTO guardarLibro(LibroCreateDTO libro);
    public List<LibroResponseDTO> listarLibros();
    public LibroResponseDTO buscarLibroPorId(ObjectId id);
    public LibroResponseDTO actualizarLibro(ObjectId id, LibroUpdateDTO libro);
    public void sincronizarTituloLibro(ObjectId id, String tituloLibro);
    public void sincronizarGenerosLibro(ObjectId id, List<GeneroLibro> generosLibro);
    public String eliminarLibro(ObjectId id);
    public List<LibroResponseDTO> listarPorGenero(GeneroLibro genero);
    public List<LibroResponseDTO> listarPorAutor(String autor);
}
