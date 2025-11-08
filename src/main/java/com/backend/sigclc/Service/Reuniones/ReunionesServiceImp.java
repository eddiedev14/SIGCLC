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
import com.backend.sigclc.Model.Reuniones.Modalidad;
import com.backend.sigclc.Model.Reuniones.ReunionesModel;
import com.backend.sigclc.Model.Reuniones.Tipo;
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
            for (String asistenteId : dto.getAsistentesId()) {
                UsuariosModel usuario = usuariosRepository.findById(new ObjectId(asistenteId))
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Error! No existe un usuario con id: " + asistenteId));

                AsistenteModel asistente = new AsistenteModel();
                asistente.setAsistenteId(usuario.getId());
                asistente.setNombreCompleto(usuario.getNombreCompleto());
                asistentes.add(asistente);
            }
            reunion.setAsistentes(asistentes);

            List<LibroSeleccionadoModel> librosSeleccionados = new ArrayList<>();
            for (String libroId : dto.getLibrosSeleccionadosId()) {
                LibrosModel libro = librosRepository.findById(new ObjectId(libroId))
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
                        case "pdf" -> adjunto.setTipo(Tipo.pdf);
                        case "jpg", "jpeg", "png" -> adjunto.setTipo(Tipo.imagen);
                        case "ppt", "pptx" -> adjunto.setTipo(Tipo.presentacion);
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
    public ReunionResponseDTO actualizarReunion(ObjectId id, ReunionUpdateDTO dto) {
        try {
            ReunionesModel reunion = reunionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe una reunión con id: " + id + " o está mal escrito."
                ));

            if (dto.getModalidad() != null && 
                !(dto.getModalidad().equals(Modalidad.presencial) || dto.getModalidad().equals(Modalidad.virtual))) {
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
                for (String asistenteId : dto.getAsistentesId()) {
                    UsuariosModel usuario = usuariosRepository.findById(new ObjectId(asistenteId))
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
                for (String libroId : dto.getLibrosSeleccionadosId()) {
                    LibrosModel libro = librosRepository.findById(new ObjectId(libroId))
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
