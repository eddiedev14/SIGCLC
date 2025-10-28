package com.backend.sigclc.Service.Libros;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;

public class LibrosServiceImp implements ILibrosService {

    @Autowired
    private ILibrosService librosService;









    @Override
    public LibroResponseDTO guardarLibro(LibroCreateDTO libro) {
        throw new UnsupportedOperationException("Unimplemented method 'guardarLibro'");
    }

    @Override
    public List<LibroResponseDTO> listarLibros() {
        throw new UnsupportedOperationException("Unimplemented method 'listarLibros'");
    }

    @Override
    public LibroResponseDTO buscarLibrosPorId(ObjectId id) {
        throw new UnsupportedOperationException("Unimplemented method 'buscarLibrosPorId'");
    }

    @Override
    public LibroResponseDTO actualizarLibro(ObjectId id, LibroUpdateDTO libro) {
        throw new UnsupportedOperationException("Unimplemented method 'actualizarLibro'");
    }

    @Override
    public String eliminarLibro(Object id) {
        throw new UnsupportedOperationException("Unimplemented method 'eliminarLibro'");
    }
    

}
