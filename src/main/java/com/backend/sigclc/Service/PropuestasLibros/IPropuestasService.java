package com.backend.sigclc.Service.PropuestasLibros;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroCreateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroResponseDTO;
import com.backend.sigclc.DTO.PropuestasLibros.PropuestaLibroUpdateDTO;
import com.backend.sigclc.DTO.PropuestasLibros.VotoDTO;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;

public interface IPropuestasService {
    public PropuestaLibroResponseDTO guardarPropuesta(PropuestaLibroCreateDTO propuesta);
    public PropuestaLibroResponseDTO votarPropuesta(ObjectId id, VotoDTO voto);
    public List<PropuestaLibroResponseDTO> listarPropuestas();
    public PropuestaLibroResponseDTO buscarPropuesta(ObjectId id);
    public List<PropuestaLibroResponseDTO> listarPropuestasPorEstado(EstadoPropuesta estado);
    public List<PropuestaLibroResponseDTO> listarPropuestasPorUsuario(ObjectId usuarioId);
    public List<PropuestaLibroResponseDTO> listarPropuestasPorLibro(ObjectId libroId);
    public PropuestaLibroResponseDTO actualizarPropuesta(ObjectId id, PropuestaLibroUpdateDTO propuesta);
    public String eliminarPropuesta(ObjectId id);
}
