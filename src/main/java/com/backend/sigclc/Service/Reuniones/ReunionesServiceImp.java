package com.backend.sigclc.Service.Reuniones;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.backend.sigclc.DTO.Reuniones.ReunionCreateDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionResponseDTO;
import com.backend.sigclc.DTO.Reuniones.ReunionUpdateDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.ReunionMapper;
import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;
import com.backend.sigclc.Model.Archivos.ArchivoAdjuntoModel;
import com.backend.sigclc.Model.PropuestasLibros.EstadoLectura;
import com.backend.sigclc.Model.Reuniones.AsistenteModel;
import com.backend.sigclc.Model.Reuniones.LibroSeleccionadoModel;
import com.backend.sigclc.Model.Reuniones.ModalidadReunion;
import com.backend.sigclc.Model.Reuniones.ReunionesModel;
import com.backend.sigclc.Model.Reuniones.TipoReunion;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Repository.IPropuestasLibrosRepository;
import com.backend.sigclc.Repository.IReunionesRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;
import com.backend.sigclc.Service.Archivos.IArchivosService;

@Service
public class ReunionesServiceImp implements IReunionesService{
    @Autowired
    private IReunionesRepository reunionesRepository;

    @Autowired
    private IPropuestasLibrosRepository propuestasLibrosRepository;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Autowired
    private IArchivosService archivosService;

    @Autowired
    private ReunionMapper reunionMapper;

    private final String CARPETA_ARCHIVOS = "archivosReunion";
    private final List<String> EXTENSIONES_PERMITIDAS = List.of(".pdf", ".jpg", ".jpeg", ".png", ".ppt", ".pptx" );

    @Override
    public ReunionResponseDTO guardarReunion(ReunionCreateDTO dto) {
        try {
            ReunionesModel reunion = reunionMapper.toModel(dto);

            // Obtener todas las propuestas una sola vez
            List<PropuestasLibrosModel> propuestas = obtenerPropuestas(dto.getLibrosSeleccionadosId());
            
            // Validar usando las propuestas ya obtenidas
            validarFechaReunionConPeriodos(reunion.getFecha(), propuestas);
            validarLibrosSeleccionadosNoLeidos(propuestas);

            List<AsistenteModel> asistentes = reunion.getAsistentes();
            for (ObjectId asistenteId : dto.getAsistentesId()) {
                UsuariosModel usuario = usuariosRepository.findById(asistenteId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Error! No existe un usuario con id: " + asistenteId));

                AsistenteModel asistente = new AsistenteModel();
                asistente.setAsistenteId(usuario.getId());
                asistente.setNombreCompleto(usuario.getNombreCompleto());
                asistentes.add(asistente);
            }
            reunion.setAsistentes(asistentes);

            // Reutilizar las propuestas ya obtenidas
            List<LibroSeleccionadoModel> librosSeleccionados = reunion.getLibrosSeleccionados();
            for (PropuestasLibrosModel propuesta : propuestas) {
                LibroSeleccionadoModel libroSel = new LibroSeleccionadoModel();
                libroSel.setPropuestaId(propuesta.getId());
                libroSel.setTitulo(propuesta.getLibroPropuesto().getTitulo());
                libroSel.setGeneros(propuesta.getLibroPropuesto().getGeneros());
                librosSeleccionados.add(libroSel);
            }
            reunion.setLibrosSeleccionados(librosSeleccionados);

            List<ArchivoAdjuntoModel> adjuntos = reunion.getArchivosAdjuntos();
            adjuntos.clear();
            if (dto.getArchivosAdjuntos() != null && !dto.getArchivosAdjuntos().isEmpty()) {
                boolean tieneArchivoValido = false;
                for (MultipartFile archivo : dto.getArchivosAdjuntos()) {

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

                reunion.setArchivosAdjuntos(adjuntos);
            }

            reunionesRepository.save(reunion);
            return reunionMapper.toResponseDTO(reunion);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la reunión", e);
        }
    }

    @Override
    public List<ReunionResponseDTO> listarReuniones() {
        List<ReunionesModel> reuniones = reunionesRepository.findAll();
        return reunionMapper.toResponseDTOList(reuniones);
    }

    @Override
    public ReunionResponseDTO buscarReunionPorId(ObjectId id) {
        ReunionesModel reunion = reunionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + id + " o está mal escrito."));
        return reunionMapper.toResponseDTO(reunion);
    }

    // ---------- AGREGACIONES ----------

    @Override
    public List<ReunionResponseDTO> listarPorAsistenteId(ObjectId asistenteId) {
        List<ReunionesModel> reuniones = reunionesRepository.buscarPorAsistenteId(asistenteId);
        return reunionMapper.toResponseDTOList(reuniones);
    }

    @Override
    public List<ReunionResponseDTO> listarPorLibroSeleccionadoId(ObjectId libroId) {
        List<ReunionesModel> reuniones = reunionesRepository.buscarPorLibroSeleccionadoId(libroId);
        return reunionMapper.toResponseDTOList(reuniones);
    }

    @Override
    public List<ReunionResponseDTO> listarPorFecha(Date fecha) {
        List<ReunionesModel> reuniones = reunionesRepository.buscarPorFecha(fecha);
        return reunionMapper.toResponseDTOList(reuniones);
    }

    @Override
    public ReunionResponseDTO actualizarReunion(ObjectId id, ReunionUpdateDTO dto) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + id + " o está mal escrito."
                ));

            if (dto.getModalidad() != null && 
                !(dto.getModalidad().equals(ModalidadReunion.presencial) || dto.getModalidad().equals(ModalidadReunion.virtual))) {
                throw new IllegalArgumentException("La modalidad solo puede ser presencial o virtual.");
            }

            if (dto.getModalidad() != null && dto.getEspacioReunion() == null) {
                throw new IllegalArgumentException("Debe especificar un espacioReunion (dirección o enlace) si cambia la modalidad.");
            }

            reunionMapper.updateModelFromDTO(reunion, dto);

            if (dto.getAsistentesId() != null) {
                List<AsistenteModel> asistentes = reunion.getAsistentes();
                asistentes.clear();

                if (!dto.getAsistentesId().isEmpty()) {
                    for (ObjectId asistenteId : dto.getAsistentesId()) {
                        UsuariosModel usuario = usuariosRepository.findById(asistenteId)
                            .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Error! No existe un usuario con id: " + asistenteId
                            ));
                        AsistenteModel asistente = new AsistenteModel();
                        asistente.setAsistenteId(usuario.getId());
                        asistente.setNombreCompleto(usuario.getNombreCompleto());
                        asistentes.add(asistente);
                    }
                }

                reunion.setAsistentes(asistentes);
            }

            if (dto.getLibrosSeleccionadosId() != null) {
                List<PropuestasLibrosModel> propuestas = obtenerPropuestas(dto.getLibrosSeleccionadosId());
                
                validarFechaReunionConPeriodos(reunion.getFecha(), propuestas);
                validarLibrosSeleccionadosNoLeidos(propuestas);

                List<LibroSeleccionadoModel> librosSeleccionados = reunion.getLibrosSeleccionados();
                librosSeleccionados.clear();

                if (!dto.getLibrosSeleccionadosId().isEmpty()) {
                    for (PropuestasLibrosModel propuesta : propuestas) {
                        LibroSeleccionadoModel libroSel = new LibroSeleccionadoModel();
                        libroSel.setPropuestaId(propuesta.getId());
                        libroSel.setTitulo(propuesta.getLibroPropuesto().getTitulo());
                        libroSel.setGeneros(propuesta.getLibroPropuesto().getGeneros());
                        librosSeleccionados.add(libroSel);
                    }
                }

                reunion.setLibrosSeleccionados(librosSeleccionados);
            }

            if (dto.getArchivosAdjuntos() != null) {
                List<ArchivoAdjuntoModel> adjuntos = reunion.getArchivosAdjuntos();
                // Eliminar archivos previos
                for (ArchivoAdjuntoModel adjunto : adjuntos) {
                    archivosService.eliminarArchivo(adjunto.getArchivoPath());
                }
                adjuntos.clear();

                if (!dto.getArchivosAdjuntos().isEmpty()) {
                    boolean tieneArchivoValido = false;
                    for (MultipartFile archivo : dto.getArchivosAdjuntos()) {
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

                reunion.setArchivosAdjuntos(adjuntos);
            }

            reunionesRepository.save(reunion);
            return reunionMapper.toResponseDTO(reunion);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO agregarLibrosAReunion(ObjectId reunionId, List<ObjectId> librosSeleccionadosId) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(reunionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
                ));

            // Obtener todas las propuestas una sola vez
            List<PropuestasLibrosModel> propuestas = obtenerPropuestas(librosSeleccionadosId);
            
            // Validar usando las propuestas ya obtenidas
            validarFechaReunionConPeriodos(reunion.getFecha(), propuestas);
            validarLibrosSeleccionadosNoLeidos(propuestas);

            List<LibroSeleccionadoModel> librosSeleccionados = reunion.getLibrosSeleccionados();

            Set<ObjectId> existentes = new HashSet<>();
            for (LibroSeleccionadoModel libro : librosSeleccionados) {
                existentes.add(libro.getPropuestaId());
            }

            // Reutilizar las propuestas ya obtenidas
            for (PropuestasLibrosModel propuesta : propuestas) {
                if (existentes.contains(propuesta.getId())) {
                    continue; 
                }

                LibroSeleccionadoModel nuevo = new LibroSeleccionadoModel();
                nuevo.setPropuestaId(propuesta.getId());
                nuevo.setTitulo(propuesta.getLibroPropuesto().getTitulo());
                nuevo.setGeneros(propuesta.getLibroPropuesto().getGeneros());
                librosSeleccionados.add(nuevo);

                existentes.add(propuesta.getId());
            }

            reunionesRepository.save(reunion);
            return reunionMapper.toResponseDTO(reunion);

        } catch (RecursoNoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al agregar libros a la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO agregarAsistentesAReunion(ObjectId reunionId, List<ObjectId> asistentesId) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(reunionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
                ));

            List<AsistenteModel> asistentes = reunion.getAsistentes();

            for (ObjectId asistenteIdStr : asistentesId) {
                boolean yaExiste = false;
                for (AsistenteModel asistente : asistentes) {
                    if (asistente.getAsistenteId().equals(asistenteIdStr)) {
                        yaExiste = true;
                        break;
                    }
                }

                if (!yaExiste) {
                    UsuariosModel usuario = usuariosRepository.findById(asistenteIdStr)
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Error! No existe un usuario con id: " + asistenteIdStr
                        ));

                    AsistenteModel nuevo = new AsistenteModel();
                    nuevo.setAsistenteId(usuario.getId());
                    nuevo.setNombreCompleto(usuario.getNombreCompleto());
                    asistentes.add(nuevo);
                }
            }

            reunionesRepository.save(reunion);
            return reunionMapper.toResponseDTO(reunion);

        } catch (RecursoNoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al agregar asistentes a la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO agregarArchivosAReunion(ObjectId id, List<MultipartFile> archivosAdjuntos) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + id + " o está mal escrito."
                ));

            List<ArchivoAdjuntoModel> adjuntos = reunion.getArchivosAdjuntos();

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

                boolean yaExiste = adjuntos.stream()
                    .anyMatch(a -> a.getArchivoPath().equals(ruta));

                if (yaExiste) {
                    continue;
                }

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

            reunionesRepository.save(reunion);

            return reunionMapper.toResponseDTO(reunion);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al agregar archivos a la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO eliminarAsistentesDeReunion(ObjectId reunionId, List<ObjectId> asistentesId) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(reunionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
                ));

            List<AsistenteModel> asistentes = reunion.getAsistentes();
            if (asistentes.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reunión no tiene asistentes registrados.");
            }

            for (ObjectId id : asistentesId) {
                asistentes.removeIf(a -> a.getAsistenteId().equals(id));
            }

            reunionesRepository.save(reunion);
            return reunionMapper.toResponseDTO(reunion);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar asistentes de la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO eliminarLibrosSeleccionadosDeReunion(ObjectId reunionId, List<ObjectId> librosId) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(reunionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
                ));

            List<LibroSeleccionadoModel> libros = reunion.getLibrosSeleccionados();
            if (libros.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reunión no tiene libros seleccionados.");
            }

            for (ObjectId id : librosId) {
                libros.removeIf(l -> l.getPropuestaId().equals(id));
            }

            reunionesRepository.save(reunion);
            return reunionMapper.toResponseDTO(reunion);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar libros de la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO eliminarArchivosDeReunion(ObjectId id, List<String> archivoUuids) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + id + " o está mal escrito."
                ));

            List<ArchivoAdjuntoModel> adjuntos = reunion.getArchivosAdjuntos();
            if (adjuntos == null || adjuntos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reunión no tiene archivos adjuntos.");
            }

            if (archivoUuids == null || archivoUuids.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe proporcionar una lista de identificadores de archivos a eliminar.");
            }

            List<String> notFound = new java.util.ArrayList<>();
            List<String> failedToDelete = new java.util.ArrayList<>();
            List<ArchivoAdjuntoModel> encontrados = new java.util.ArrayList<>();

            for (String uuid : archivoUuids) {
                ArchivoAdjuntoModel encontrado = adjuntos.stream()
                    .filter(a -> a.getArchivoPath() != null && a.getArchivoPath().contains(uuid))
                    .findFirst()
                    .orElse(null);
                if (encontrado == null) {
                    notFound.add(uuid);
                } else {
                    encontrados.add(encontrado);
                }
            }

            if (encontrados.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron archivos adjuntos para los identificadores proporcionados: " + notFound);
            }

            for (ArchivoAdjuntoModel a : encontrados) {
                try {
                    archivosService.eliminarArchivo(a.getArchivoPath());
                    adjuntos.remove(a);
                } catch (Exception e) {
                    failedToDelete.add(a.getArchivoPath());
                }
            }

            reunionesRepository.save(reunion);

            if (!failedToDelete.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Algunos archivos no se pudieron eliminar físicamente: " + failedToDelete);
            }

            return reunionMapper.toResponseDTO(reunion);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar archivo de la reunión", e);
        }
    }

    @Override
    public String eliminarReunion(ObjectId id) {
        ReunionesModel reunion = reunionesRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reunión con id: " + id + " o está mal escrito."));

        LocalDate fechaReunion = reunion.getFecha().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        if (fechaReunion.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede eliminar una reunión cuya fecha ya ha pasado.");
        }

        reunionesRepository.delete(reunion);

        return "Reunión eliminada correctamente con id: " + id;
    }

    private void validarFechaReunionConPeriodos(Date fechaReunion, List<PropuestasLibrosModel> propuestas) {
        if (propuestas == null || propuestas.isEmpty()) {
            return; 
        }

        Date fechaInterseccionInicio = null;
        Date fechaInterseccionFin = null;

        // Iterar sobre cada período de selección para encontrar la intersección
        for (PropuestasLibrosModel propuesta : propuestas) {
            if (propuesta.getPeriodoSeleccion() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "La propuesta con id: " + propuesta.getId() + " no tiene período de selección asignado.");
            }

            Date periodoInicio = propuesta.getPeriodoSeleccion().getFechaInicio();
            Date periodoFin = propuesta.getPeriodoSeleccion().getFechaFin();

            if (fechaInterseccionInicio == null) {
                fechaInterseccionInicio = periodoInicio;
                fechaInterseccionFin = periodoFin;
            } else {
                // Actualizar la intersección: tomar el máximo de los inicios y el mínimo de los finales
                if (periodoInicio.after(fechaInterseccionInicio)) {
                    fechaInterseccionInicio = periodoInicio;
                }
                if (periodoFin.before(fechaInterseccionFin)) {
                    fechaInterseccionFin = periodoFin;
                }
            }
        }

        // Validar que la fecha de la reunión esté dentro de la intersección
        if (fechaReunion.before(fechaInterseccionInicio) || fechaReunion.after(fechaInterseccionFin)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "La fecha de la reunión debe estar entre el " + fechaInterseccionInicio + 
                " y el " + fechaInterseccionFin + " (períodos de selección de los libros).");
        }
    }

    private void validarLibrosSeleccionadosNoLeidos(List<PropuestasLibrosModel> propuestas) {
        if (propuestas == null || propuestas.isEmpty()) {
            return;
        }

        for (PropuestasLibrosModel propuesta : propuestas) {
            if (propuesta.getEstadoPropuesta() != EstadoPropuesta.seleccionada) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La propuesta con id: " + propuesta.getId() + " debe estar en estado 'seleccionada' para agregarse a una reunión.");
            }

            EstadoLectura estadoLectura = propuesta.getLibroPropuesto().getEstadoLectura();
            if (estadoLectura == null || estadoLectura == EstadoLectura.leido) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El libro de la propuesta con id: " + propuesta.getId() + " debe estar en estado de lectura 'pendiente' o 'en_lectura' (no leído).");
            }
        }
    }


    private List<PropuestasLibrosModel> obtenerPropuestas(List<ObjectId> propuestasId) {
        if (propuestasId == null || propuestasId.isEmpty()) {
            return List.of();
        }

        List<PropuestasLibrosModel> propuestas = new java.util.ArrayList<>();
        for (ObjectId propuestaId : propuestasId) {
            PropuestasLibrosModel propuesta = propuestasLibrosRepository.findById(propuestaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una propuesta con id: " + propuestaId));
            propuestas.add(propuesta);
        }
        return propuestas;
    }
}
