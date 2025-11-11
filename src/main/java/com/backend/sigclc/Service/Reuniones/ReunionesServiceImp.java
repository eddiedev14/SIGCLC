package com.backend.sigclc.Service.Reuniones;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Model.Reuniones.ArchivoAdjuntoModel;
import com.backend.sigclc.Model.Reuniones.AsistenteModel;
import com.backend.sigclc.Model.Reuniones.LibroSeleccionadoModel;
import com.backend.sigclc.Model.Reuniones.ModalidadReunion;
import com.backend.sigclc.Model.Reuniones.ReunionesModel;
import com.backend.sigclc.Model.Reuniones.TipoReunion;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Repository.ILibrosRepository;
import com.backend.sigclc.Repository.IReunionesRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;
import com.backend.sigclc.Service.Archivos.IArchivosService;

@Service
public class ReunionesServiceImp implements IReunionesService{
    @Autowired
    private IReunionesRepository reunionesRepository;

    @Autowired
    private ILibrosRepository librosRepository;

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

            List<AsistenteModel> asistentes = new ArrayList<>();
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

            List<LibroSeleccionadoModel> librosSeleccionados = new ArrayList<>();
            for (ObjectId libroId : dto.getLibrosSeleccionadosId()) {
                LibrosModel libro = librosRepository.findById(libroId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Error! No existe un libro con id: " + libroId));

                LibroSeleccionadoModel libroSel = new LibroSeleccionadoModel();
                libroSel.setPropuestaId(libro.getId());
                libroSel.setTitulo(libro.getTitulo());
                libroSel.setGeneros(libro.getGeneros());
                librosSeleccionados.add(libroSel);
            }
            reunion.setLibrosSeleccionados(librosSeleccionados);

            List<ArchivoAdjuntoModel> adjuntos = new ArrayList<>();
            if (dto.getArchivosAdjuntos() != null && !dto.getArchivosAdjuntos().isEmpty()) {
                boolean tieneArchivoValido = false;
                for (MultipartFile archivo : dto.getArchivosAdjuntos()) {
                    System.out.println("Archivo recibido: " + archivo.getOriginalFilename() + " | vacío: " + archivo.isEmpty());
                    if (archivo == null || archivo.isEmpty()) {
                        throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Se detectó un campo de archivo vacío. Todos los archivos deben tener contenido."
                    );
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
public ReunionResponseDTO agregarLibrosAReunion(ObjectId reunionId, List<String> librosSeleccionadosId) {
    try {
        ReunionesModel reunion = reunionesRepository.findById(reunionId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
            ));

        if (reunion.getLibrosSeleccionados() == null) {
            reunion.setLibrosSeleccionados(new ArrayList<>());
        }

        for (String libroIdStr : librosSeleccionadosId) {
            ObjectId libroId = new ObjectId(libroIdStr);

            boolean yaExiste = reunion.getLibrosSeleccionados().stream()
                .anyMatch(lib -> lib.getPropuestaId().equals(libroId));

            if (!yaExiste) {
                LibrosModel libro = librosRepository.findById(libroId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Error! No existe un libro con id: " + libroId
                    ));

                LibroSeleccionadoModel nuevo = new LibroSeleccionadoModel();
                nuevo.setPropuestaId(libro.getId());
                nuevo.setTitulo(libro.getTitulo());
                nuevo.setGeneros(libro.getGeneros());

                reunion.getLibrosSeleccionados().add(nuevo);
            }
        }

        reunionesRepository.save(reunion);

        return reunionMapper.toResponseDTO(reunion);

    } catch (RecursoNoEncontradoException e) {
        throw e;
    } catch (Exception e) {
        e.printStackTrace();
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al agregar libros a la reunión", e);
    }
    }

    @Override
    public ReunionResponseDTO agregarAsistentesAReunion(ObjectId reunionId, List<String> asistentesId) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(reunionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
                ));

            if (reunion.getAsistentes() == null) {
                reunion.setAsistentes(new ArrayList<>());
            }

            for (String asistenteIdStr : asistentesId) {
                ObjectId asistenteId = new ObjectId(asistenteIdStr);

                boolean yaExiste = reunion.getAsistentes().stream()
                    .anyMatch(a -> a.getAsistenteId().equals(asistenteId));

                if (!yaExiste) {
                    UsuariosModel usuario = usuariosRepository.findById(asistenteId)
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Error! No existe un usuario con id: " + asistenteId
                        ));

                    AsistenteModel nuevo = new AsistenteModel();
                    nuevo.setAsistenteId(usuario.getId());
                    nuevo.setNombreCompleto(usuario.getNombreCompleto());

                    reunion.getAsistentes().add(nuevo);
                }
            }

            reunionesRepository.save(reunion);

            return reunionMapper.toResponseDTO(reunion);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al agregar asistentes a la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO eliminarAsistentesDeReunion(ObjectId reunionId, List<String> asistentesId) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(reunionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
                ));

            if (reunion.getAsistentes() == null || reunion.getAsistentes().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reunión no tiene asistentes registrados.");
            }

            List<ObjectId> idsAEliminar = asistentesId.stream().map(ObjectId::new).toList();

            reunion.setAsistentes(
                reunion.getAsistentes().stream()
                    .filter(a -> !idsAEliminar.contains(a.getAsistenteId()))
                    .toList()
            );

            reunionesRepository.save(reunion);

            return reunionMapper.toResponseDTO(reunion);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar asistentes de la reunión", e);
        }
    }

    @Override
    public ReunionResponseDTO eliminarLibrosSeleccionadosDeReunion(ObjectId reunionId, List<String> librosId) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(reunionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + reunionId + " o está mal escrito."
                ));

            if (reunion.getLibrosSeleccionados() == null || reunion.getLibrosSeleccionados().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reunión no tiene libros seleccionados.");
            }

            List<ObjectId> idsAEliminar = librosId.stream().map(ObjectId::new).toList();

            reunion.setLibrosSeleccionados(
                reunion.getLibrosSeleccionados().stream()
                    .filter(l -> !idsAEliminar.contains(l.getPropuestaId()))
                    .toList()
            );

            reunionesRepository.save(reunion);
            return reunionMapper.toResponseDTO(reunion);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar libros de la reunión", e);
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

    @Override
    public ReunionResponseDTO agregarArchivosAReunion(ObjectId id, List<MultipartFile> archivosAdjuntos) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + id + " o está mal escrito."
                ));

            if (reunion.getArchivosAdjuntos() == null) {
                reunion.setArchivosAdjuntos(new ArrayList<>());
            }

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

                boolean yaExiste = reunion.getArchivosAdjuntos().stream()
                    .anyMatch(a -> a.getArchivoPath().equals(ruta));

                if (!yaExiste) {
                    reunion.getArchivosAdjuntos().add(adjunto);
                }
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

            if (dto.getFecha() != null) reunion.setFecha(dto.getFecha());
            if (dto.getHora() != null) reunion.setHora(dto.getHora());
            if (dto.getModalidad() != null) reunion.setModalidad(dto.getModalidad());
            if (dto.getEspacioReunion() != null) reunion.setEspacioReunion(dto.getEspacioReunion());

            if (dto.getAsistentesId() != null) {
                List<AsistenteModel> asistentes = new ArrayList<>();
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
                reunion.setAsistentes(asistentes);
            }

            if (dto.getLibrosSeleccionadosId() != null) {
                List<LibroSeleccionadoModel> librosSeleccionados = new ArrayList<>();
                for (ObjectId libroId : dto.getLibrosSeleccionadosId()) {
                    LibrosModel libro = librosRepository.findById(libroId)
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Error! No existe un libro con id: " + libroId
                        ));
                    LibroSeleccionadoModel libroSel = new LibroSeleccionadoModel();
                    libroSel.setPropuestaId(libro.getId());
                    libroSel.setTitulo(libro.getTitulo());
                    libroSel.setGeneros(libro.getGeneros());
                    librosSeleccionados.add(libroSel);
                }
                reunion.setLibrosSeleccionados(librosSeleccionados);
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


}
