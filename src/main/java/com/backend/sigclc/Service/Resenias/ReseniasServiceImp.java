package com.backend.sigclc.Service.Resenias;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioCreateDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionCreateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionUpdateDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.ReseniaMapper;
import com.backend.sigclc.Model.Archivos.ArchivoAdjuntoModel;
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Model.Resenias.ComentadorModel;
import com.backend.sigclc.Model.Resenias.ComentarioModel;
import com.backend.sigclc.Model.Resenias.ReseniaModel;
import com.backend.sigclc.Model.Resenias.ValoracionModel;
import com.backend.sigclc.Model.Resenias.ValoradorModel;
import com.backend.sigclc.Model.Reuniones.TipoReunion;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;

import com.backend.sigclc.Repository.ILibrosRepository;
import com.backend.sigclc.Repository.IReseniasRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;
import com.backend.sigclc.Service.Archivos.IArchivosService;

@Service
public class ReseniasServiceImp implements IReseniasService {
    @Autowired
    private IReseniasRepository reseniasRepository;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Autowired
    private ILibrosRepository librosRepository;

    @Autowired
    private ReseniaMapper reseniaMapper;

    @Autowired
    private IArchivosService archivosService;

    private final String CARPETA_ARCHIVOS = "archivosResenia";
    private final List<String> EXTENSIONES_PERMITIDAS = List.of(".pdf", ".jpg", ".jpeg", ".png", ".ppt", ".pptx" );

    @Override
    public ReseniaResponseDTO crearResenia(ReseniaCreateDTO reseniaCreateDTO) {
        ReseniaModel reseniaModel = reseniaMapper.toReseniaModel(reseniaCreateDTO);

        // Definir campos con valores por defecto
        reseniaModel.setFechaPublicacion(new Date());

        // Añadir campos automáticamente a redactor con base al usuario
        ObjectId usuarioId = reseniaModel.getRedactor().getUsuarioId();
        UsuariosModel usuario = usuariosRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + usuarioId + " o está mal escrito."));
        reseniaModel.getRedactor().setNombreCompleto(usuario.getNombreCompleto());

        // Añadir campos automáticamente al libro reseñado con base al libro
        ObjectId libroId = reseniaModel.getLibro().getLibroId();
        LibrosModel libro = librosRepository.findById(libroId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un libro con id: " + libroId + " o está mal escrito."));
        reseniaModel.getLibro().setTitulo(libro.getTitulo());

        // Guardar los archivos
        List<ArchivoAdjuntoModel> adjuntos = reseniaModel.getArchivosAdjuntos();
        if (reseniaCreateDTO.getArchivosAdjuntos() != null && !reseniaCreateDTO.getArchivosAdjuntos().isEmpty()) {
            boolean tieneArchivoValido = false;
            for (MultipartFile archivo : reseniaCreateDTO.getArchivosAdjuntos()) {

                tieneArchivoValido = true;
                String ruta = archivosService.guardarArchivo(
                    archivo,
                    CARPETA_ARCHIVOS,
                    EXTENSIONES_PERMITIDAS
                );

                ArchivoAdjuntoModel adjunto = new ArchivoAdjuntoModel();
                adjunto.setArchivoPath(ruta);

                String extension = archivosService.obtenerExtensionSinPunto(ruta);
                switch (extension.toLowerCase()) {
                    case "pdf" -> adjunto.setTipo(TipoReunion.pdf);
                    case "jpg", "jpeg", "png" -> adjunto.setTipo(TipoReunion.imagen);
                    case "ppt", "pptx" -> adjunto.setTipo(TipoReunion.presentacion);
                    default -> throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Tipo de archivo no reconocido: " + extension);
                }
                adjuntos.add(adjunto);
            }
            
            if (!tieneArchivoValido) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe adjuntar al menos un archivo válido.");
            }

            reseniaModel.setArchivosAdjuntos(adjuntos);
        }

        // Guardar la resenia
        reseniasRepository.save(reseniaModel);

        // Retornar DTO
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public List<ReseniaResponseDTO> listarResenias() {
        List<ReseniaModel> resenias = reseniasRepository.findAll();
        return reseniaMapper.toResponseDTOList(resenias);
    }

    @Override
    public ReseniaResponseDTO buscarReseniaPorId(ObjectId id) {
        ReseniaModel reseniaModel = reseniasRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + id + " o está mal escrito."));
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public List<ReseniaResponseDTO> buscarReseniasPorRedactor(ObjectId redactorId) {
        List<ReseniaModel> resenias = reseniasRepository.buscarReseniasPorRedactor(redactorId);
        return reseniaMapper.toResponseDTOList(resenias);
    }

    @Override
    public List<ReseniaResponseDTO> buscarReseniasPorLibro(ObjectId libroId) {
        List<ReseniaModel> resenias = reseniasRepository.buscarReseniasPorLibro(libroId);
        return reseniaMapper.toResponseDTOList(resenias);
    }

    @Override
    public ReseniaResponseDTO actualizarResenia(ObjectId reseniaId, ReseniaUpdateDTO reseniaUpdateDTO) {
        ReseniaModel reseniaModel = reseniasRepository.findById(reseniaId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + reseniaId + " o está mal escrito."));
        
        reseniaMapper.updateReseniaModelFromDTO(reseniaModel, reseniaUpdateDTO);

        // Actualizar el titulo del libro reseñado automáticamente si se modifico
        if (reseniaUpdateDTO.getLibroReseniado() != null && reseniaUpdateDTO.getLibroReseniado().getLibroId() != null) {
            LibrosModel libro = librosRepository.findById(reseniaUpdateDTO.getLibroReseniado().getLibroId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe un libro con id: " + reseniaUpdateDTO.getLibroReseniado().getLibroId() + " o está mal escrito."));
            reseniaModel.getLibro().setTitulo(libro.getTitulo());
        }

        // Actualizar y guardar archivos
        if (reseniaUpdateDTO.getArchivosAdjuntos() != null) {
            List<ArchivoAdjuntoModel> adjuntos = reseniaModel.getArchivosAdjuntos();
            // Eliminar archivos previos
            for (ArchivoAdjuntoModel adjunto : adjuntos) {
                archivosService.eliminarArchivo(adjunto.getArchivoPath());
            }
            adjuntos.clear();

            if (!reseniaUpdateDTO.getArchivosAdjuntos().isEmpty()) {
                boolean tieneArchivoValido = false;
                for (MultipartFile archivo : reseniaUpdateDTO.getArchivosAdjuntos()) {
                    if (archivo == null || archivo.isEmpty()) {
                        continue;
                    }
                    tieneArchivoValido = true;

                    String ruta = archivosService.guardarArchivo(
                        archivo,
                        CARPETA_ARCHIVOS,
                        EXTENSIONES_PERMITIDAS
                    );

                    ArchivoAdjuntoModel adjunto = new ArchivoAdjuntoModel();
                    adjunto.setArchivoPath(ruta);

                    String extension = archivosService.obtenerExtensionSinPunto(ruta);
                    switch (extension.toLowerCase()) {
                        case "pdf" -> adjunto.setTipo(TipoReunion.pdf);
                        case "jpg", "jpeg", "png" -> adjunto.setTipo(TipoReunion.imagen);
                        case "ppt", "pptx" -> adjunto.setTipo(TipoReunion.presentacion);
                        default -> throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Tipo de archivo no reconocido: " + extension);
                    }
                    adjuntos.add(adjunto);
                }

                if (!tieneArchivoValido) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe adjuntar al menos un archivo válido.");
                }
            }

            reseniaModel.setArchivosAdjuntos(adjuntos);
        }

        // Colocar automaticamente el nombre de las valoraciones
        if (reseniaUpdateDTO.getValoraciones() != null) {
            List<ValoracionModel> valoraciones = reseniaModel.getValoraciones();
            valoraciones.clear();
            
            if (!reseniaUpdateDTO.getValoraciones().isEmpty()) {
                for (ValoracionCreateDTO valoracionDTO : reseniaUpdateDTO.getValoraciones()) {
                    //* Un usuario solo puede valorar una vez */
                    if (valoraciones.stream().anyMatch(v -> v.getValorador().getUsuarioId().equals(valoracionDTO.getUsuarioId()))) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario con id " + valoracionDTO.getUsuarioId() + " ya ha valorado esta reseña.");
                    }
                    
                    UsuariosModel usuario = usuariosRepository.findById(valoracionDTO.getUsuarioId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Error! No existe un usuario con id: " + valoracionDTO.getUsuarioId() + " o está mal escrito."));
                    ValoracionModel valoracion = new ValoracionModel();
                    valoracion.setValorador(new ValoradorModel(usuario.getId(), usuario.getNombreCompleto()));
                    valoracion.setFecha(new Date());
                    valoracion.setValoracion(valoracionDTO.getValoracion());
                    valoraciones.add(valoracion);
                }
            }

            reseniaModel.setValoraciones(valoraciones);
        }

        // Colocar automáticamente el nombre de los comentarios
        if (reseniaUpdateDTO.getComentarios() != null) {
            List<ComentarioModel> comentarios = reseniaModel.getComentarios();
            comentarios.clear();
            
            if (!reseniaUpdateDTO.getComentarios().isEmpty()) {
                for (ComentarioCreateDTO comentarioDTO : reseniaUpdateDTO.getComentarios()) {
                    UsuariosModel usuario = usuariosRepository.findById(comentarioDTO.getUsuarioId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Error! No existe un usuario con id: " + comentarioDTO.getUsuarioId() + " o está mal escrito."));
                    ComentarioModel comentario = new ComentarioModel();
                    comentario.setComentarioId(new ObjectId());
                    comentario.setComentador(new ComentadorModel(usuario.getId(), usuario.getNombreCompleto()));
                    comentario.setFecha(new Date());
                    comentario.setComentario(comentarioDTO.getComentario());
                    comentarios.add(comentario);
                }
            }

            reseniaModel.setComentarios(comentarios);
        }

        // Guardar la reseña
        reseniasRepository.save(reseniaModel);
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public ReseniaResponseDTO valorarResenia(ObjectId reseniaId, ValoracionCreateDTO valoracionCreateDTO) {
        ReseniaModel reseniaModel = reseniasRepository.findById(reseniaId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + reseniaId + " o está mal escrito."));
        
        UsuariosModel usuario = usuariosRepository.findById(valoracionCreateDTO.getUsuarioId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + valoracionCreateDTO.getUsuarioId() + " o está mal escrito."));

        //* Un usuario solo puede valorar una vez */
        if (reseniaModel.getValoraciones().stream()
            .anyMatch(valoracion -> valoracion.getValorador().getUsuarioId().equals(usuario.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya ha valorado esta reseña.");
        }

        ValoracionModel valoracion = new ValoracionModel();
        valoracion.setValorador(new ValoradorModel(usuario.getId(), usuario.getNombreCompleto()));
        valoracion.setFecha(new Date());
        valoracion.setValoracion(valoracionCreateDTO.getValoracion());
        reseniaModel.getValoraciones().add(valoracion);
        
        reseniasRepository.save(reseniaModel);
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public ReseniaResponseDTO comentarResenia(ObjectId reseniaId, ComentarioCreateDTO comentarioCreateDTO) {
        ReseniaModel reseniaModel = reseniasRepository.findById(reseniaId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + reseniaId + " o está mal escrito."));
        
        UsuariosModel usuario = usuariosRepository.findById(comentarioCreateDTO.getUsuarioId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + comentarioCreateDTO.getUsuarioId() + " o está mal escrito."));

        ComentarioModel comentario = new ComentarioModel();
        comentario.setComentarioId(new ObjectId());
        comentario.setComentador(new ComentadorModel(usuario.getId(), usuario.getNombreCompleto()));
        comentario.setFecha(new Date());
        comentario.setComentario(comentarioCreateDTO.getComentario());
        reseniaModel.getComentarios().add(comentario);
        
        reseniasRepository.save(reseniaModel);
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public ReseniaResponseDTO agregarArchivosAResenia(ObjectId reseniaId, List<MultipartFile> archivosAdjuntos) {
        ReseniaModel reseniaModel = reseniasRepository.findById(reseniaId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + reseniaId + " o está mal escrito."));

        List<ArchivoAdjuntoModel> adjuntos = reseniaModel.getArchivosAdjuntos();
        boolean tieneArchivoValido = false;

        for (MultipartFile archivo : archivosAdjuntos) {
            if (archivo == null || archivo.isEmpty()) {
                continue;
            }

            tieneArchivoValido = true;

            String ruta = archivosService.guardarArchivo(
                archivo,
                CARPETA_ARCHIVOS,
                EXTENSIONES_PERMITIDAS
            );

            ArchivoAdjuntoModel adjunto = new ArchivoAdjuntoModel();
            adjunto.setArchivoPath(ruta);

            String extension = archivosService.obtenerExtensionSinPunto(ruta);
            switch (extension.toLowerCase()) {
                case "pdf" -> adjunto.setTipo(TipoReunion.pdf);
                case "jpg", "jpeg", "png" -> adjunto.setTipo(TipoReunion.imagen);
                case "ppt", "pptx" -> adjunto.setTipo(TipoReunion.presentacion);
                default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Tipo de archivo no reconocido: " + extension);
            }
            adjuntos.add(adjunto);

        }

        if (!tieneArchivoValido) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe adjuntar al menos un archivo válido.");
        }

        // Guardar la reseña
        reseniasRepository.save(reseniaModel);
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public ReseniaResponseDTO actualizarValoracion(ObjectId reseniaId, ObjectId usuarioId, ValoracionUpdateDTO valoracionUpdateDTO) {
        ReseniaModel reseniaModel = reseniasRepository.findById(reseniaId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + reseniaId + " o está mal escrito."));
        
        UsuariosModel usuario = usuariosRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + usuarioId + " o está mal escrito."));

        //* Validar que dicho usuario haya valorado la reseña (sino, mostrar error) *
        if (reseniaModel.getValoraciones().stream()
            .noneMatch(valoracion -> valoracion.getValorador().getUsuarioId().equals(usuario.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no ha valorado esta reseña.");
        }

        //* Obtener valoracion de ese usuario */
        ValoracionModel valoracion = reseniaModel.getValoraciones().stream()
            .filter(v -> v.getValorador().getUsuarioId().equals(usuario.getId()))
            .findFirst()
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una valoración con id: " + usuarioId + " o está mal escrito."));

        //* Actualizar valoracion *
        valoracion.setValoracion(valoracionUpdateDTO.getValoracion());

        //* Guardar la reseña *
        reseniasRepository.save(reseniaModel);
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public ReseniaResponseDTO actualizarComentario(ObjectId reseniaId, ObjectId comentarioId, ComentarioUpdateDTO comentarioUpdateDTO) {
        ReseniaModel reseniaModel = reseniasRepository.findById(reseniaId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + reseniaId + " o está mal escrito."));
        
        ComentarioModel comentario = reseniaModel.getComentarios().stream()
            .filter(c -> c.getComentarioId().equals(comentarioId))
            .findFirst()
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un comentario con id: " + comentarioId + " o está mal escrito."));

        //* Actualizar comentario *
        comentario.setComentario(comentarioUpdateDTO.getComentario());

        //* Guardar la reseña *
        reseniasRepository.save(reseniaModel);
        return reseniaMapper.toResponseDTO(reseniaModel);
    }

    @Override
    public ReseniaResponseDTO eliminarValoracion(ObjectId reseniaId, ObjectId usuarioId) {
        ReseniaModel reseniaModel = reseniasRepository.findById(reseniaId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reseña con id: " + reseniaId + " o está mal escrito."));
        
        ValoracionModel valoracion = reseniaModel.getValoraciones().stream()
            .filter(v -> v.getValorador().getUsuarioId().equals(usuarioId))
            .findFirst()
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una valoración con id: " + usuarioId + " o está mal escrito."));

        //* Eliminar valoración *
        reseniaModel.getValoraciones().remove(valoracion);

        //* Guardar la reseña *
        reseniasRepository.save(reseniaModel);
        return reseniaMapper.toResponseDTO(reseniaModel);
    }
}
