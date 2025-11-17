package com.backend.sigclc.Service.RetosLectura;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.sigclc.DTO.RetosLectura.RetoLecturaCreateDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaResponseDTO;
import com.backend.sigclc.DTO.RetosLectura.RetoLecturaUpdateDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoLibroDTO;
import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoResponseDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.RetoLecturaMapper;
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Model.RetosLectura.LibrosAsociadosModel;
import com.backend.sigclc.Model.RetosLectura.ProgresoLibroModel;
import com.backend.sigclc.Model.RetosLectura.ProgresoModel;
import com.backend.sigclc.Model.RetosLectura.RetosLecturaModel;
import com.backend.sigclc.Model.RetosLectura.UsuariosInscritosModel;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Repository.ILibrosRepository;
import com.backend.sigclc.Repository.IRetosLecturaRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;

@Service
public class RetosLecturaServiceImp implements IRetosLecturaService{
    @Autowired
    private RetoLecturaMapper retoLecturaMapper;

    @Autowired
    private IRetosLecturaRepository retosLecturaRepository;

    @Autowired
    private ILibrosRepository librosRepository;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Override
    public RetoLecturaResponseDTO crearRetoLectura(RetoLecturaCreateDTO retoLectura) {
        RetosLecturaModel model = retoLecturaMapper.toModel(retoLectura);

        // Traer los datos de los libros asociados de la base de datos
        if (model.getLibrosAsociados() != null && !model.getLibrosAsociados().isEmpty()) {
            model.getLibrosAsociados().forEach(libroAsociado -> {
                ObjectId libroId = libroAsociado.getLibroId();
                LibrosModel libro = librosRepository.findById(libroId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Error! No existe un libro con id: " + libroId + " o está mal escrito."));
                
                libroAsociado.setTitulo(libro.getTitulo());
                libroAsociado.setGeneros(libro.getGeneros());
            });
        }

        RetosLecturaModel saved = retosLecturaRepository.save(model);

        return retoLecturaMapper.toResponseDTO(saved);
    }
    
    @Override
    public RetoLecturaResponseDTO registrarProgreso(ObjectId retoId, ObjectId usuarioId, ProgresoDTO progreso) {
        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un reto con id: " + retoId));
        
        Date hoy = new Date();

        if (reto.getFechaInicio().after(hoy)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se puede registrar el progreso pues el reto no ha comenzado.");
            }
        
        if (reto.getFechaFinalizacion().before(hoy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se puede registrar el progreso pues el reto ya ha finalizado");
        }

        UsuariosInscritosModel usuarioInscrito = reto.getUsuariosInscritos().stream()
            .filter(usuario -> usuario.getUsuarioId().equals(usuarioId))
            .findFirst()
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no está inscrito en este reto: " + usuarioId));

        // Buscar o crear el progreso para dicho libro
        ProgresoModel progresoModel = usuarioInscrito.getProgreso().stream()
            .filter(p -> p.getLibroAsociadoId().equals(progreso.getLibroAsociadoId()))
            .findFirst()
            .orElseGet(() -> {
                ProgresoModel modelProgreso = new ProgresoModel();
                modelProgreso.setLibroAsociadoId(progreso.getLibroAsociadoId());
                usuarioInscrito.getProgreso().add(modelProgreso);
                return modelProgreso;
            });

        if (progreso.getProgresoLibro() != null) {
            for (ProgresoLibroDTO progresoLibroDto : progreso.getProgresoLibro()) {
                ProgresoLibroModel progresoMdl = new ProgresoLibroModel();
                progresoMdl.setFecha(new Date());
                progresoMdl.setObservacion(progresoLibroDto.getObservacion());
                progresoModel.getProgresoLibro().add(progresoMdl);
            }
        }

        RetosLecturaModel saved = retosLecturaRepository.save(reto);
        return retoLecturaMapper.toResponseDTO(saved);
    }

    @Override
    public List<RetoLecturaResponseDTO> listarRetosLectura() {
        List<RetosLecturaModel> retos = retosLecturaRepository.findAll();
        return retoLecturaMapper.toResponseDTOList(retos);
    }

    @Override
    public RetoLecturaResponseDTO buscarRetoLectura(ObjectId retoId) {
        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
        .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un reto de lectura con id: " + retoId + " o está mal escrito."));;
        return retoLecturaMapper.toResponseDTO(reto);
    }

    @Override
    public String eliminarRetoLectura(ObjectId retoId) {
        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un reto de lectura con id: " + retoId + " o está mal escrito."));

        validarFechaInicioNoPasada(reto);

        retosLecturaRepository.delete(reto);
        return "Reto de lectura eliminado correctamente.";
    }

    private void validarFechaInicioNoPasada(RetosLecturaModel reto) {
        if (reto.getFechaInicio() != null && reto.getFechaInicio().before(new Date())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "No se puede modificar reto de lectura porque la fecha de inicio ya ha pasado."
            );
        }
    }

    @Override
    public RetoLecturaResponseDTO actualizarReto(ObjectId retoId, RetoLecturaUpdateDTO dto) {
        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un reto con id: " + retoId));

        Date hoy = new Date();

        if (reto.getFechaFinalizacion().before(hoy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se puede modificar un reto que ya finalizó.");
        }

        if (dto.getTitulo() != null && dto.getTitulo().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El título no puede estar vacío.");
        }

        if (dto.getDescripcion() != null && dto.getDescripcion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La descripción no puede estar vacía.");
        }

        boolean vieneFechaInicio = dto.getFechaInicio() != null;
        boolean vieneFechaFin = dto.getFechaFinalizacion() != null;

        if (vieneFechaInicio && vieneFechaFin) {

            if (reto.getFechaInicio().before(hoy)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se puede modificar el periodo del reto porque ya comenzó.");
            }

            if (dto.getFechaInicio().before(hoy)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La nueva fecha de inicio no puede ser anterior a la fecha actual.");
            }

            if (!dto.getFechaInicio().before(dto.getFechaFinalizacion())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La fecha de inicio debe ser menor que la fecha de finalización.");
            }

        }

        if (dto.getLibrosAsociadosId() != null) {
            List<LibrosAsociadosModel> nuevos = dto.getLibrosAsociadosId().stream().map(id -> {
                LibrosModel libro = librosRepository.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Error! No existe un libro con id: " + id));

                LibrosAsociadosModel model = new LibrosAsociadosModel();
                model.setLibroId(id);
                model.setTitulo(libro.getTitulo());
                model.setGeneros(libro.getGeneros());
                return model;
            }).toList();

            reto.setLibrosAsociados(nuevos); 
        }

        // Traer los datos del usuario inscrito desde la base de datos
        if (dto.getUsuariosInscritosId() != null) {
            List<UsuariosInscritosModel> nuevos = dto.getUsuariosInscritosId().stream().map(id -> {
                UsuariosModel u = usuariosRepository.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Error! No existe un usuario con id: " + id));

                UsuariosInscritosModel model = new UsuariosInscritosModel();
                model.setUsuarioId(id);
                model.setNombreCompleto(u.getNombreCompleto());
                return model;
            }).toList();

            reto.setUsuariosInscritos(nuevos); 
        }

        retoLecturaMapper.updateModelFromDTO(reto, dto);

        RetosLecturaModel saved = retosLecturaRepository.save(reto);
        return retoLecturaMapper.toResponseDTO(saved);
    }

    @Override
    public RetoLecturaResponseDTO agregarUsuarios(ObjectId retoId, List<ObjectId> usuariosIds) {

        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un reto con id: " + retoId));

        validarFechaInicioNoPasada(reto);


        for (ObjectId usuarioId : usuariosIds) {

            boolean yaExiste = reto.getUsuariosInscritos().stream()
                .anyMatch(u -> u.getUsuarioId().equals(usuarioId));

            if (yaExiste) continue; 

            UsuariosModel usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No existe un usuario con id: " + usuarioId));

            UsuariosInscritosModel nuevo = new UsuariosInscritosModel();
            nuevo.setUsuarioId(usuarioId);
            nuevo.setNombreCompleto(usuario.getNombreCompleto());

            reto.getUsuariosInscritos().add(nuevo);
        }

        RetosLecturaModel saved = retosLecturaRepository.save(reto);
        return retoLecturaMapper.toResponseDTO(saved);
    }

    @Override
    public RetoLecturaResponseDTO eliminarUsuarios(ObjectId retoId, List<ObjectId> usuariosIds) {

        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un reto con id: " + retoId));

        validarFechaInicioNoPasada(reto);

        reto.getUsuariosInscritos().removeIf(u ->
            usuariosIds.contains(u.getUsuarioId())
        );

        RetosLecturaModel saved = retosLecturaRepository.save(reto);
        return retoLecturaMapper.toResponseDTO(saved);
    }

    @Override
    public RetoLecturaResponseDTO agregarLibros(ObjectId retoId, List<ObjectId> librosIds) {

        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un reto con id: " + retoId));

        validarFechaInicioNoPasada(reto);

        for (ObjectId libroId : librosIds) {

            boolean yaExiste = reto.getLibrosAsociados().stream()
                .anyMatch(l -> l.getLibroId().equals(libroId));

            if (yaExiste) continue;

            LibrosModel libro = librosRepository.findById(libroId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No existe un libro con id: " + libroId));

            LibrosAsociadosModel nuevo = new LibrosAsociadosModel();
            nuevo.setLibroId(libroId);
            nuevo.setTitulo(libro.getTitulo());
            nuevo.setGeneros(libro.getGeneros());

            reto.getLibrosAsociados().add(nuevo);
        }

        RetosLecturaModel saved = retosLecturaRepository.save(reto);
        return retoLecturaMapper.toResponseDTO(saved);
    }


    @Override
    public RetoLecturaResponseDTO eliminarLibros(ObjectId retoId, List<ObjectId> librosIds) {

        RetosLecturaModel reto = retosLecturaRepository.findById(retoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un reto con id: " + retoId));

        validarFechaInicioNoPasada(reto);

        reto.getLibrosAsociados().removeIf(l ->
            librosIds.contains(l.getLibroId())
        );

        RetosLecturaModel saved = retosLecturaRepository.save(reto);
        return retoLecturaMapper.toResponseDTO(saved);
    }

    @Override
    public List<RetoLecturaResponseDTO> buscarRetosPorUsuario(ObjectId usuarioId) {
        List<RetosLecturaModel> retos = retosLecturaRepository.buscarRetosPorUsuario(usuarioId);
        if (retos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                "No se encontraron retos donde el usuario esté inscrito: " + usuarioId);
        }
        return retoLecturaMapper.toResponseDTOList(retos);
    }

    @Override
    public List<RetoLecturaResponseDTO> buscarRetosPorLibro(ObjectId libroId) {
        List<RetosLecturaModel> retos = retosLecturaRepository.buscarRetosPorLibro(libroId);
        if (retos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                "No se encontraron retos asociados al libro con id: " + libroId);
        }
        return retoLecturaMapper.toResponseDTOList(retos);
    }

    @Override
    public List<RetoLecturaResponseDTO> buscarRetosActivos() {
        Date hoy = new Date(); 
        List<RetosLecturaModel> retos = retosLecturaRepository.buscarRetosActivos(hoy);
        if (retos.isEmpty()) {
            throw new RecursoNoEncontradoException("No hay retos activos actualmente.");
        }
        return retoLecturaMapper.toResponseDTOList(retos);
    }

    @Override
    public ProgresoResponseDTO buscarProgreso(ObjectId retoId, ObjectId usuarioId, ObjectId libroAsociadoId) {

        RetosLecturaModel resultado = retosLecturaRepository.buscarProgreso(retoId, usuarioId, libroAsociadoId);

        if (resultado == null) {
            throw new RecursoNoEncontradoException(
                "No se encontró progreso para el usuario " + usuarioId +
                " con el libro " + libroAsociadoId + " en el reto " + retoId
            );
        }

        // Mongo devuelve solo el usuario que coincide
        UsuariosInscritosModel usuario = resultado.getUsuariosInscritos().get(0);

        ProgresoModel progreso = usuario.getProgreso().stream()
            .filter(p -> p.getLibroAsociadoId().equals(libroAsociadoId))
            .findFirst()
            .orElseThrow(() -> new RecursoNoEncontradoException(
                    "El usuario no tiene progreso registrado para el libro: " + libroAsociadoId
            ));

        // Convertir el modelo a un DTO usando tu mapper
        return new ProgresoResponseDTO(
            progreso.getLibroAsociadoIdAString(),
            retoLecturaMapper.toProgresoLibroResponseDTOList(progreso.getProgresoLibro())
        );
    }

}
