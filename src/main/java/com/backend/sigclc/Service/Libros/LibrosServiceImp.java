package com.backend.sigclc.Service.Libros;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.sigclc.DTO.Libros.LibroCreateDTO;
import com.backend.sigclc.DTO.Libros.LibroResponseDTO;
import com.backend.sigclc.DTO.Libros.LibroUpdateDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.LibroMapper;
import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Repository.ILibrosRepository;
import com.backend.sigclc.Repository.IPropuestasLibrosRepository;
import com.backend.sigclc.Repository.IReseniasRepository;
import com.backend.sigclc.Repository.IRetosLecturaRepository;
import com.backend.sigclc.Repository.IReunionesRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;
import com.backend.sigclc.Service.Archivos.ArchivosServiceImp;

@Service
public class LibrosServiceImp implements ILibrosService {

    @Autowired
    private ILibrosRepository librosRepository;

    @Autowired
    private IPropuestasLibrosRepository propuestasLibrosRepository;

    @Autowired
    private IReunionesRepository reunionesRepository;

    @Autowired
    private IUsuariosRepository usuarioRepository;

    @Autowired
    private IReseniasRepository reseniasRepository;

    @Autowired
    private IRetosLecturaRepository retosLecturaRepository;

    @Autowired
    private LibroMapper libroMapper;

    @Autowired
    private ArchivosServiceImp archivoService;

    private final String CARPETA_PORTADAS = "portadas";
    private final List<String> EXTENSIONES_PERMITIDAS = List.of(".jpg", ".jpeg", ".png");

    @Override
    public LibroResponseDTO guardarLibro(LibroCreateDTO dto) {
        try {
            String rutaPortada = null;

            // Guardar la imagen si se adjuntó una
            if (dto.getImagen() != null && !dto.getImagen().isEmpty()) {
                rutaPortada = archivoService.guardarArchivo(dto.getImagen(), CARPETA_PORTADAS, EXTENSIONES_PERMITIDAS);
            }

            // Crear modelo de libro
            LibrosModel libro = libroMapper.toModel(dto);

            // Añadir nombre completo del creador automáticamente
            ObjectId usuarioId = libro.getCreador().getUsuarioId();
            UsuariosModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe un usuario con id: " + usuarioId + " o está mal escrito."));

            libro.getCreador().setNombreCompleto(usuario.getNombreCompleto());

            // Asignar la ruta de la portada si existe
            if (rutaPortada != null) {
                libro.setPortadaPath(rutaPortada);
            }

            // Guardar en la base de datos
            librosRepository.save(libro);
            return libroMapper.toResponseDTO(libro);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el libro", e);
        }
    }

    @Override
    public List<LibroResponseDTO> listarLibros() {
        return libroMapper.toResponseDTOList(librosRepository.findAll());
    }

    @Override
    public LibroResponseDTO buscarLibroPorId(ObjectId id) {
        LibrosModel libro = librosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un libro con id: " + id + " o está mal escrito."));
        return libroMapper.toResponseDTO(libro);
    }

    @Override
    public LibroResponseDTO actualizarLibro(ObjectId id, LibroUpdateDTO dto) {
        LibrosModel libro = librosRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No se pudo encontrar el libro con el ID especificado"));

        // Actualiza los campos del libro (excepto la imagen)
        libroMapper.updateModelFromDTO(dto, libro);

        try {
            // Si hay una nueva imagen, reemplazar la existente
            if (dto.getImagen() != null && !dto.getImagen().isEmpty()) {
                // Eliminar la anterior si existe
                archivoService.eliminarArchivo(libro.getPortadaPath());

                // Guardar la nueva
                String nuevaRuta = archivoService.guardarArchivo(dto.getImagen(), CARPETA_PORTADAS, EXTENSIONES_PERMITIDAS);
                libro.setPortadaPath(nuevaRuta);
            }

            // Guardar los cambios
            librosRepository.save(libro);

            // Propagar cambios de título y géneros
            if (dto.getTitulo() != null) {
                sincronizarTituloLibro(id, dto.getTitulo());
            }

            if (dto.getGeneros() != null) {
                sincronizarGenerosLibro(id, dto.getGeneros());
            }

            return libroMapper.toResponseDTO(libro);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el libro", e);
        }
    }

    @Override
    public String eliminarLibro(ObjectId id) {
        LibrosModel libro = librosRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No se pudo encontrar el libro con el ID especificado"));

            //* Validaciones previas a la eliminación */

            // Verificar si el libro esta asociada a una propuesta (sin importar su estado)
            if (propuestasLibrosRepository.existsByLibroPropuestoLibroId(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro no puede ser eliminado porque tiene propuestas asociadas");
            }

            // Verificar si el libro esta asociada a una reseña
            if (reseniasRepository.existsByLibroId(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro no puede ser eliminado porque tiene reseñas asociadas");
            }

            // Verificar si el libro estpa asociado a retos de lectura
            if (retosLecturaRepository.existsByLibroAsociado(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro no puede ser eliminado porque tiene retos de lectura asociados");
            }

            // Eliminar archivo físico de portada
            archivoService.eliminarArchivo(libro.getPortadaPath());

            librosRepository.deleteById(id);
            return "Libro eliminado correctamente.";
    }

    @Override
    public void sincronizarTituloLibro(ObjectId id, String tituloLibro) {
        propuestasLibrosRepository.actualizarTituloLibroPropuesto(id, tituloLibro);
        reunionesRepository.actualizarTituloLibroSeleccionado(id, tituloLibro);
        reseniasRepository.actualizarTituloLibroReseniado(id, tituloLibro);
        retosLecturaRepository.actualizarTituloLibroAsociado(id, tituloLibro);
    }

    @Override
    public void sincronizarGenerosLibro(ObjectId id, List<GeneroLibro> generosLibro) {
        propuestasLibrosRepository.actualizarGenerosLibroPropuesto(id, generosLibro);
        reunionesRepository.actualizarGenerosLibroSeleccionado(id, generosLibro);
        retosLecturaRepository.actualizarGenerosLibro(id, generosLibro);
    }

    @Override
    public List<LibroResponseDTO> listarPorGenero(GeneroLibro genero) {
        return libroMapper.toResponseDTOList(librosRepository.buscarPorGenero(genero));
    }

    @Override
    public List<LibroResponseDTO> listarPorAutor(String autor) {
        autor = autor.replace("-", " ");
        return libroMapper.toResponseDTOList(librosRepository.buscarPorAutor(autor));
    }
}
