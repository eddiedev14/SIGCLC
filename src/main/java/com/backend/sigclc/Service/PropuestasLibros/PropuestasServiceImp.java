package com.backend.sigclc.Service.PropuestasLibros;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroUpdateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.Votos.VotoDTO;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarPropuesta'");
    }

    @Override
    public String eliminarPropuesta(ObjectId id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarPropuesta'");
    }
    
}
