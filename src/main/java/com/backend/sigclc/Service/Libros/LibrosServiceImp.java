package com.backend.sigclc.Service.Libros;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.LibroMapper;
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Repository.ILibrosRepository;

public class LibrosServiceImp implements ILibrosService {

    @Autowired
    private ILibrosRepository librosRepository;

    @Autowired
    private LibroMapper libroMapper;

    @Override
    public LibroResponseDTO guardarLibro(LibroCreateDTO libro) {
        LibrosModel model = libroMapper.toModel(libro);
        librosRepository.save(model);
        return libroMapper.toResponseDTO(model);
    }

    @Override
    public List<LibroResponseDTO> listarLibros() {
        List<LibrosModel> libros = librosRepository.findAll();
        return libroMapper.toResponseDTOList(libros);
    }

    @Override
    public LibroResponseDTO buscarLibrosPorId(ObjectId id) {
        LibrosModel libro = librosRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "Error! No existe un libro con id: " + id + " o está mal escrito."));
        return libroMapper.toResponseDTO(libro);
    }

    @Override
    public LibroResponseDTO actualizarLibro(ObjectId id, LibroUpdateDTO dto) {
        LibrosModel existente = librosRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "No se encontró el libro con id: " + id));
        
        libroMapper.updateModelFromDTO(dto, existente);
        LibrosModel actualizado = librosRepository.save(existente);
        return libroMapper.toResponseDTO(actualizado);
    }

    @Override
    public String eliminarLibro(ObjectId id) {
        if (!librosRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No se encontró el libro con id: " + id);
        }
        librosRepository.deleteById(id);
        return "Libro eliminado correctamente con id: " + id;
    }
    

}
