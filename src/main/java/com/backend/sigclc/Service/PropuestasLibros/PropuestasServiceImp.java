package com.backend.sigclc.Service.PropuestasLibros;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroUpdateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.VotoDTO;
import com.backend.sigclc.Mapper.PropuestaLibroMapper;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;
import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;
import com.backend.sigclc.Repository.IPropuestasLibrosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropuestasServiceImp implements IPropuestasService {
    @Autowired 
    private IPropuestasLibrosRepository propuestasLibrosRepository;

    @Autowired 
    private PropuestaLibroMapper propuestaLibrosMapper;

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
