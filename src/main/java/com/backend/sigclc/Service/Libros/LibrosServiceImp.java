package com.backend.sigclc.Service.Libros;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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
import com.backend.sigclc.Repository.IUsuariosRepository;

@Service
public class LibrosServiceImp implements ILibrosService {

    @Autowired
    private ILibrosRepository librosRepository;

    @Autowired
    private IPropuestasLibrosRepository propuestasLibrosRepository;

    @Autowired
    private IUsuariosRepository usuarioRepository;

    @Autowired
    private LibroMapper libroMapper;

    private final Path rootFolder = Paths.get("uploads/portadas");

    @Override
    public LibroResponseDTO guardarLibro(LibroCreateDTO dto) {
        try {
            // Se asegura que la carpeta exista
            if (!Files.exists(rootFolder)) {
                Files.createDirectories(rootFolder);
            }

            String nombreArchivo = null;

            // Valida el formato de la imagen y la guarda
            if (dto.getImagen() != null && !dto.getImagen().isEmpty()) {
                String originalName = dto.getImagen().getOriginalFilename();
                String extension = "";

                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
                }

                List<String> extensionesPermitidas = List.of(".jpg", ".jpeg", ".png");
                if (!extensionesPermitidas.contains(extension)) {
                    throw new IllegalArgumentException("Solo se permiten imágenes con extensión .jpg, .jpeg o .png");
                }

                nombreArchivo = UUID.randomUUID() + extension;
                Path destino = rootFolder.resolve(nombreArchivo);
                Files.copy(dto.getImagen().getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            }

            // Crea el modelo del libro a partir del dto
            LibrosModel libro = libroMapper.toModel(dto);

            //* Añadir nombre completo del creador automáticamente */
            ObjectId usuarioId = dto.getCreador().getUsuarioId();
            UsuariosModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe un usuario con id: " + usuarioId + " o está mal escrito."));
            
            libro.getCreador().setNombreCompleto(usuario.getNombreCompleto());
            
            // en caso de que si se haya subido la imagen se establece el portadaPath
            if (nombreArchivo != null) {
                libro.setPortadaPath("uploads/portadas/" + nombreArchivo);
            }

            // se guarda el libro
            librosRepository.save(libro);
            return libroMapper.toResponseDTO(libro);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen de portada", e);
        }
    }

    @Override
    public List<LibroResponseDTO> listarLibros() {
        List<LibrosModel> libros = librosRepository.findAll();
        return libroMapper.toResponseDTOList(libros);
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
                        HttpStatus.NOT_FOUND, "No se pudo encontrar el libro con el ID especificado"
                ));

        //Actualiza los campos del libro que son formatos normales, no la imagen
        libroMapper.updateModelFromDTO(dto, libro);

        try {
            if (dto.getImagen() != null && !dto.getImagen().isEmpty()) {
                //En caso de que ya existía una imagen, se borra para poner la nueva
                if (libro.getPortadaPath() != null) {
                    Path anterior = Paths.get(libro.getPortadaPath());
                    Files.deleteIfExists(anterior);
                }

                // Si no existe la carpeta se crea
                if (!Files.exists(rootFolder)) {
                    Files.createDirectories(rootFolder);
                }

                String originalName = dto.getImagen().getOriginalFilename();
                String extension = "";

                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
                }

                List<String> extensionesPermitidas = List.of(".jpg", ".jpeg", ".png");
                if (!extensionesPermitidas.contains(extension)) {
                    throw new IllegalArgumentException("Solo se permiten imágenes con extensión .jpg, .jpeg o .png");
                }

                // En este apartado se guarda dicha imagen
                String nuevoArchivo = UUID.randomUUID() + extension;
                Path destino = rootFolder.resolve(nuevoArchivo);
                Files.copy(dto.getImagen().getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

                // Se actualiza el portadaPath con la nueva ruta de la imagen
                libro.setPortadaPath("uploads/portadas/" + nuevoArchivo);
            }

            // Actualizar libro
            librosRepository.save(libro);

            // Propagar cambio de nombre en caso de que haya sido modificado a otras colecciones
            if (dto.getTitulo() != null) {
                sincronizarTituloLibro(id, dto.getTitulo());
            }

            // Propagar cambio de generos en caso de que haya sido modificado a otras colecciones
            if (dto.getGeneros() != null) {
                sincronizarGenerosLibro(id, dto.getGeneros());
            }

            return libroMapper.toResponseDTO(libro);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar la solicitud", e);
        }
    }

    @Override
    public void sincronizarTituloLibro(ObjectId id, String tituloLibro) {
        propuestasLibrosRepository.actualizarTituloLibroPropuesto(id, tituloLibro);
    }   

    @Override
    public void sincronizarGenerosLibro(ObjectId id, List<GeneroLibro> generosLibro) {
        propuestasLibrosRepository.actualizarGenerosLibroPropuesto(id, generosLibro);
    }

    @Override
    public String eliminarLibro(ObjectId id) {
        LibrosModel libro = librosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se pudo encontrar el libro con el ID especificado"
                ));

        try {
            if (libro.getPortadaPath() != null && !libro.getPortadaPath().isBlank()) {
                // Construir ruta absoluta de forma correcta
                Path rutaPortada = rootFolder.resolve(
                    Paths.get(libro.getPortadaPath()).getFileName().toString()
                );

                // Verificar si existe antes de borrar
                if (Files.exists(rutaPortada)) {
                    Files.delete(rutaPortada);
                    System.out.println("Imagen eliminada correctamente");
                } else {
                    System.out.println("No se encontró la imagen");
                }
            }

            // * Un libro solo puede ser eliminado si no está asociado a ninguna otra colección
            boolean tienePropuestasEnVotacion = propuestasLibrosRepository.tienePropuestasEnVotacion(id);

            if (tienePropuestasEnVotacion) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro no puede ser eliminado porque tiene propuestas en votación o está asociado a otros registros");
            }

            librosRepository.deleteById(id);
            return "Libro eliminado correctamente.";

        } catch (IOException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error al eliminar la imagen o el libro", e
            );
        }
    }

    @Override
    public List<LibroResponseDTO> listarPorGenero(GeneroLibro genero) {
        List<LibrosModel> libros = librosRepository.buscarPorGenero(genero);
        return libroMapper.toResponseDTOList(libros);
    }

    @Override
    public List<LibroResponseDTO> listarPorAutor(String autor) {
        // Quitar guiones del nombre del autor
        autor = autor.replace("-", " ");
        List<LibrosModel> libros = librosRepository.buscarPorAutor(autor);
        return libroMapper.toResponseDTOList(libros);
    }
    

}
