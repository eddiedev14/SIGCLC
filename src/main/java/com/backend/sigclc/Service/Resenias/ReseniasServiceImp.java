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
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.ReseniaMapper;
import com.backend.sigclc.Model.Archivos.ArchivoAdjuntoModel;
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Model.Resenias.ReseniaModel;
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
}
