package com.backend.sigclc.Service.PropuestasLibros;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroUpdateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.VotoDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.PropuestaLibroMapper;
import com.backend.sigclc.Model.Libros.LibrosModel;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;
import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;
import com.backend.sigclc.Model.PropuestasLibros.VotoModel;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Repository.ILibrosRepository;
import com.backend.sigclc.Repository.IPropuestasLibrosRepository;
import com.backend.sigclc.Repository.IUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropuestasServiceImp implements IPropuestasService {
    @Autowired 
    private IPropuestasLibrosRepository propuestasLibrosRepository;

    @Autowired 
    private PropuestaLibroMapper propuestaLibrosMapper;

    @Autowired
    private ILibrosRepository librosRepository;

    @Autowired
    private IUsuariosRepository usuariosRepository;
    
    @Override
    public PropuestaLibroResponseDTO guardarPropuesta(PropuestaLibroCreateDTO propuesta) {
        PropuestasLibrosModel model = propuestaLibrosMapper.toModel(propuesta);

        // Definir campos con valores por defecto SOLO CUANDO SE CREA UNA NUEVA PROPUESTA
        model.setFechaPropuesta(new Date());
        model.setEstadoPropuesta(EstadoPropuesta.en_votacion);
        model.setVotos(new ArrayList<>());
        model.setPeriodoSeleccion(null);

        //* Añadir campos automáticamente a libroPropuesto con base al libro */
        ObjectId libroId = model.getLibroPropuesto().getLibroId();
        LibrosModel libro = librosRepository.findById(libroId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un libro con id: " + libroId + " o está mal escrito."));
        
        model.getLibroPropuesto().setTitulo(libro.getTitulo());
        model.getLibroPropuesto().setGeneros(libro.getGeneros());

        //* Añadir campos automáticamente a usuarioProponente con base al usuario */
        ObjectId usuarioId = model.getUsuarioProponente().getUsuarioId();
        UsuariosModel usuario = usuariosRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + usuarioId + " o está mal escrito."));

        model.getUsuarioProponente().setNombreCompleto(usuario.getNombreCompleto());

        // Guardar propuesta
        propuestasLibrosRepository.save(model);

        return propuestaLibrosMapper.toResponseDTO(model);
    }

    @Override
    public PropuestaLibroResponseDTO votarPropuesta(ObjectId id, VotoDTO voto) {
        // Obtener la propuesta
        PropuestasLibrosModel propuesta = propuestasLibrosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una propuesta con id: " + id + " o está mal escrito."));

        // Solo se puede votar si el estado_propuesta es en_votacion
        if (propuesta.getEstadoPropuesta() != EstadoPropuesta.en_votacion) {
            throw new IllegalArgumentException("Solo se puede votar si el estado de la propuesta es en_votacion");
        }

        // Validar que el usuario no haya votado previamente
        if (propuesta.getVotos() != null) {
            for (VotoModel votoModel : propuesta.getVotos()) {
                if (votoModel.getUsuarioId().equals(voto.getUsuarioId())) {
                    throw new IllegalArgumentException("El usuario ya ha votado previamente");
                }
            }
        }

        // Agregar voto
        VotoModel votoModel = propuestaLibrosMapper.toVotoModel(voto);

        // Setear fecha del voto con la fecha actual
        votoModel.setFechaVoto(new Date());

        //* Añadir campos automáticamente a votos con base al usuario */
        ObjectId votoUsuarioId = voto.getUsuarioId();
        UsuariosModel usuarioVoto = usuariosRepository.findById(votoUsuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + votoUsuarioId + " o está mal escrito."));
        votoModel.setNombreCompleto(usuarioVoto.getNombreCompleto());

        // Agregar voto
        propuesta.getVotos().add(votoModel);

        // Guardar propuesta
        propuestasLibrosRepository.save(propuesta);

        return propuestaLibrosMapper.toResponseDTO(propuesta);
    }

    @Override
    public List<PropuestaLibroResponseDTO> listarPropuestas() {
        List<PropuestasLibrosModel> propuestas = propuestasLibrosRepository.findAll();
        return propuestaLibrosMapper.toResponseDTOList(propuestas);
    }

    @Override
    public PropuestaLibroResponseDTO buscarPropuesta(ObjectId id) {
        PropuestasLibrosModel propuesta = propuestasLibrosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una propuesta con id: " + id + " o está mal escrito."));
        return propuestaLibrosMapper.toResponseDTO(propuesta);
    }

    @Override
    public List<PropuestaLibroResponseDTO> listarPropuestasPorEstado(EstadoPropuesta estado) {
        List<PropuestasLibrosModel> propuestas = propuestasLibrosRepository.buscarPropuestasPorEstado(estado);
        return propuestaLibrosMapper.toResponseDTOList(propuestas);
    }

    @Override
    public List<PropuestaLibroResponseDTO> listarPropuestasPorUsuario(ObjectId usuarioId) {
        List<PropuestasLibrosModel> propuestas = propuestasLibrosRepository.buscarPropuestasPorUsuario(usuarioId);
        return propuestaLibrosMapper.toResponseDTOList(propuestas);
    }

    @Override
    public List<PropuestaLibroResponseDTO> listarPropuestasPorLibro(ObjectId libroId) {
        List<PropuestasLibrosModel> propuestas = propuestasLibrosRepository.buscarPropuestasPorLibro(libroId);
        return propuestaLibrosMapper.toResponseDTOList(propuestas);
    }

    @Override
    public PropuestaLibroResponseDTO actualizarPropuesta(ObjectId id, PropuestaLibroUpdateDTO propuesta) {
        // Obtener la propuesta del libro
        PropuestasLibrosModel propuestaModel = propuestasLibrosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe una propuesta con id: " + id + " o está mal escrito."));
        
        // * Validaciones
        
        // Almacenar estado previo/original de la propuesta
        EstadoPropuesta estadoOriginal = propuestaModel.getEstadoPropuesta();

        // Si el estado original es distinto a seleccionado, y se quiere cambiar a seleccionado, debe de pasarse en el DTO periodoSeleccion y/o estadoLectura
        if (estadoOriginal != EstadoPropuesta.seleccionada && propuesta.getEstadoPropuesta() == EstadoPropuesta.seleccionada && (propuesta.getPeriodoSeleccion() == null || propuesta.getEstadoLectura() == null)) {
            throw new IllegalArgumentException("Para cambiar el estado de la propuesta a seleccionada, debe de pasarse el periodoSeleccion y/o el estadoLectura");
        }

        // Si el estado original es distinto a seleccionado, y se pasa en el DTO el periodoSeleccion o estadoLectura y no se pasa el estadoPropuesta a seleccionada, se debe de lanzar una excepción
        if (estadoOriginal != EstadoPropuesta.seleccionada && (propuesta.getPeriodoSeleccion() != null || propuesta.getEstadoLectura() != null) && propuesta.getEstadoPropuesta() != EstadoPropuesta.seleccionada) {
            throw new IllegalArgumentException("Para cambiar el periodoSeleccion y/o el estadoLectura, debe de pasarse el estadoPropuesta a seleccionada");
        }

        // Si el estado original es seleccionado, y se quiere cambiar a distinto de seleccionado (transición explícita en el DTO), no se debe pasar en el DTO periodoSeleccion y/o estadoLectura.
        if (estadoOriginal == EstadoPropuesta.seleccionada && propuesta.getEstadoPropuesta() != null && propuesta.getEstadoPropuesta() != EstadoPropuesta.seleccionada && (propuesta.getPeriodoSeleccion() != null || propuesta.getEstadoLectura() != null)) {
            throw new IllegalArgumentException("Para cambiar el estado de la propuesta a " + propuesta.getEstadoPropuesta() + ", no se debe pasar el periodoSeleccion y/o el estadoLectura");
        }

        // Si la fechaInicio es mayor a la fechaFin del periodoSeleccion, se debe de lanzar una excepción
        if (propuesta.getPeriodoSeleccion() != null && propuesta.getPeriodoSeleccion().getFechaInicio().after(propuesta.getPeriodoSeleccion().getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio del periodo de seleccion debe de ser anterior a la fecha de fin");
        }
        
        // * Actualizar propuesta de libro
        propuestaLibrosMapper.updateModelFromDTO(propuesta, propuestaModel);

        // En caso de que el nuevo estado es seleccionado y el original no lo era, se setea la fechaSeleccion con la fecha actual
        if (propuestaModel.getEstadoPropuesta() == EstadoPropuesta.seleccionada && estadoOriginal != EstadoPropuesta.seleccionada) {
            propuestaModel.getLibroPropuesto().setFechaSeleccion(new Date());
        }

        // En caso de que se pasa a un estado distinto a seleccionado y el estado previo es seleccionado, se setea la fechaSeleccion, periodoSeleccion y estadoLectura a null
        if (propuestaModel.getEstadoPropuesta() != EstadoPropuesta.seleccionada && estadoOriginal == EstadoPropuesta.seleccionada) {
            propuestaModel.getLibroPropuesto().setFechaSeleccion(null);
            propuestaModel.setPeriodoSeleccion(null);
            propuestaModel.getLibroPropuesto().setEstadoLectura(null);
        }

        // Guardar propuesta
        propuestasLibrosRepository.save(propuestaModel);

        return propuestaLibrosMapper.toResponseDTO(propuestaModel);
    }

    @Override
    public String eliminarPropuesta(ObjectId id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarPropuesta'");
    }
    
}
