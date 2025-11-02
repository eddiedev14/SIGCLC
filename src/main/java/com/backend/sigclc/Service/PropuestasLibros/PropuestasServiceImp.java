package com.backend.sigclc.Service.PropuestasLibros;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

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
        //* Validaciones del DTO

        // Si el estadoLectura es seleccionada, entonces el periodoSeleccion debe ser obligatorio
        if (propuesta.getEstadoPropuesta() == EstadoPropuesta.seleccionada && propuesta.getPeriodoSeleccion() == null) {
            throw new IllegalArgumentException("Debe incluirse un periodoSeleccion cuando la propuesta está en estado 'SELECCIONADA'");
        }

        // Si el estadoLectura es seleccionada, entonces fechaSeleccion y estadoLectura es obligatorio
        if (propuesta.getEstadoPropuesta() == EstadoPropuesta.seleccionada && (propuesta.getLibroPropuesto().getFechaSeleccion() == null || propuesta.getLibroPropuesto().getEstadoLectura() == null)) {
            throw new IllegalArgumentException("Debe incluirse una fechaSeleccion y estadoLectura cuando la propuesta está en estado 'SELECCIONADA'");
        }

        // Si el estadoLectura es seleccionada, entonces debe de tener al menos un voto
        if (propuesta.getEstadoPropuesta() == EstadoPropuesta.seleccionada && propuesta.getVotos() == null || propuesta.getVotos().isEmpty()) {
            throw new IllegalArgumentException("Debe incluirse al menos un voto cuando la propuesta está en estado 'SELECCIONADA'");
        }

        // No pueden haber votos con una fecha anterior a la fecha de propuesta
        for (VotoDTO voto : propuesta.getVotos()) {
            if (voto.getFechaVoto().before(propuesta.getFechaPropuesta())) {
                throw new IllegalArgumentException("No pueden haber votos con una fecha anterior a la fecha de propuesta");
            }
        }   

        // Un usuario que ya votó no puede votar otra vez
        if (propuesta.getVotos() != null) {
            // Crear un set (Estructura de datos que no permite duplicados)
            Set<ObjectId> usuariosVotantes = new HashSet<>();
            for (VotoDTO voto : propuesta.getVotos()) {
                ObjectId usuarioId = voto.getUsuarioId();
                // Si ocurre un error al agregar un usuarioId al set, significa que ya existe
                if (!usuariosVotantes.add(usuarioId)) {
                    throw new IllegalArgumentException("Un usuario no puede votar más de una vez en la misma propuesta");
                }
            }
        }

        // Si el estadoLectura es no seleccionada o en_votacion, entonces fechaSeleccion y estadoLectura debe ser nulo
        if (propuesta.getEstadoPropuesta() != EstadoPropuesta.seleccionada && (propuesta.getLibroPropuesto().getFechaSeleccion() != null || propuesta.getLibroPropuesto().getEstadoLectura() != null)) {
            throw new IllegalArgumentException("La fechaSeleccion y estadoLectura deben ser nulos cuando la propuesta no está en estado 'SELECCIONADA'");
        }

        // Si el estadoLectura es no seleccionada o en_votacion, entonces el periodoSeleccion debe ser nulo
        if (propuesta.getEstadoPropuesta() != EstadoPropuesta.seleccionada && propuesta.getPeriodoSeleccion() != null) {
            throw new IllegalArgumentException("El periodoSeleccion debe ser nulo cuando la propuesta no está en estado 'SELECCIONADA'");
        }

        // Si el periodo de seleccion se pasa, la fechaFin debe ser mayor a la fechaInicio
        if (propuesta.getPeriodoSeleccion() != null && propuesta.getPeriodoSeleccion().getFechaFin().before(propuesta.getPeriodoSeleccion().getFechaInicio())) {
            throw new IllegalArgumentException("La fechaFin debe ser mayor a la fechaInicio");
        }
        
        PropuestasLibrosModel model = propuestaLibrosMapper.toModel(propuesta);

        //* Añadir campos automáticamente a libroPropuesto con base al libro */
        ObjectId libroId = propuesta.getLibroPropuesto().getLibroId();
        LibrosModel libro = librosRepository.findById(libroId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un libro con id: " + libroId + " o está mal escrito."));
        
        model.getLibroPropuesto().setTitulo(libro.getTitulo());
        model.getLibroPropuesto().setGeneros(libro.getGeneros());

        //* Añadir campos automáticamente a usuarioProponente con base al usuario */
        ObjectId usuarioId = propuesta.getUsuarioProponente().getUsuarioId();
        UsuariosModel usuario = usuariosRepository.findById(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + usuarioId + " o está mal escrito."));

        model.getUsuarioProponente().setNombreCompleto(usuario.getNombreCompleto());

        //* Añadir campos automáticamente a votos con base al voto */
        List<VotoModel> votos = new ArrayList<>();
        for (VotoDTO voto : propuesta.getVotos()) {
            ObjectId votoUsuarioId = voto.getUsuarioId();
            UsuariosModel usuarioVoto = usuariosRepository.findById(votoUsuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Error! No existe un usuario con id: " + votoUsuarioId + " o está mal escrito."));
            votos.add(new VotoModel(votoUsuarioId, usuarioVoto.getNombreCompleto(), voto.getFechaVoto()));
        }

        model.setVotos(votos);

        // Guardar propuesta
        propuestasLibrosRepository.save(model);

        return propuestaLibrosMapper.toResponseDTO(model);
    }

    @Override
    public PropuestaLibroResponseDTO agregarVoto(ObjectId id, VotoDTO voto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'agregarVoto'");
    }

    @Override
    public List<PropuestaLibroResponseDTO> listarPropuestas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarPropuestas'");
    }

    @Override
    public PropuestaLibroResponseDTO actualizarPropuesta(ObjectId id, PropuestaLibroUpdateDTO propuesta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarPropuesta'");
    }

    @Override
    public String eliminarPropuesta(ObjectId id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarPropuesta'");
    }
    
}
