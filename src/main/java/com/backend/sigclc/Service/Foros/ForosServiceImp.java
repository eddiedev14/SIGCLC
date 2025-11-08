package com.backend.sigclc.Service.Foros;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.sigclc.DTO.Foros.ForoCreateDTO;
import com.backend.sigclc.DTO.Foros.ForoResponseDTO;
import com.backend.sigclc.DTO.Foros.ForoUpdateDTO;
import com.backend.sigclc.Exception.RecursoNoEncontradoException;
import com.backend.sigclc.Mapper.ForoMapper;
import com.backend.sigclc.Model.Foros.ForosModel;
import com.backend.sigclc.Model.Foros.TipoTematica;
import com.backend.sigclc.Model.Usuarios.RolUsuario;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Repository.IForosRepository;

@Service
public class ForosServiceImp implements IForosService {

    @Autowired
    private IForosRepository forosRepository;

    @Autowired
    private ForoMapper foroMapper;

    @Autowired
    private com.backend.sigclc.Repository.IUsuariosRepository usuariosRepository;

    @Override
    public ForoResponseDTO guardarForo(ForoCreateDTO foro) {
        // Convertir DTO a modelo
        ForosModel model = foroMapper.toModel(foro);

        // Asignar fecha actual
        model.setFechaPublicacion(new Date());

        // Obtener el ID del moderador desde el DTO
        ObjectId moderadorId = model.getModerador().getModeradorId();

        // Buscar el usuario en la colección 'usuarios'
        UsuariosModel moderador = usuariosRepository.findById(moderadorId)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Error! No existe un usuario con id: " + moderadorId));

        // Verificar que el rol del usuario sea MODERADOR
        if (moderador.getRol() != RolUsuario.moderador) {
            throw new RecursoNoEncontradoException(
                "Error! El usuario con id " + moderadorId + " no tiene rol de moderador.");
        }

        // Asignar datos del moderador al foro
        model.getModerador().setNombreCompleto(moderador.getNombreCompleto());

        // Guardar foro en Mongo
        forosRepository.save(model);

        // Devolver DTO de respuesta
        return foroMapper.toResponseDTO(model);
    }

    @Override
    public List<ForoResponseDTO> listarForos() {
        List<ForosModel> foros = forosRepository.findAll();
        return foroMapper.toResponseDTOList(foros);
    }   

    @Override
    public ForoResponseDTO buscarForoPorId(ObjectId id) {
        ForosModel foro = forosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado con ID: " + id));
        return foroMapper.toResponseDTO(foro);
    }

    @Override
    public ForoResponseDTO listarPorNombreTematica(String nombreTematica) {
        Optional<ForosModel> foro = forosRepository.buscarPorNombreTematica(nombreTematica);
        return foro.map(foroMapper::toResponseDTO)
                   .orElseThrow(() -> new RuntimeException("No se encontró foro con nombre de temática: " + nombreTematica));
    }

    @Override
    public List<ForoResponseDTO> listarPorTipoTematica(TipoTematica tipoTematica) {
        List<ForosModel> foros = forosRepository.buscarPorTipoTematica(tipoTematica);
        return foroMapper.toResponseDTOList(foros);
    }

    @Override
    public List<ForoResponseDTO> listarPorModerador(ObjectId moderadorId) {
        List<ForosModel> foros = forosRepository.buscarPorModerador(moderadorId);
        return foroMapper.toResponseDTOList(foros);
    }

    @Override
    public List<ForoResponseDTO> listarForosPublicadosDespues(Date fecha) {
        List<ForosModel> foros = forosRepository.buscarForosPublicadosDespues(fecha);
        return foroMapper.toResponseDTOList(foros);
    }

    @Override
    public List<ForoResponseDTO> listarForosPublicadosAntes(Date fecha) {
        List<ForosModel> foros = forosRepository.buscarForosPublicadosAntes(fecha);
        return foroMapper.toResponseDTOList(foros);
    }

    @Override
    public ForoResponseDTO actualizarForo(ObjectId id, ForoUpdateDTO foroDTO) {
        ForosModel foro = forosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró foro con ID: " + id));
        
        foroMapper.updateModelFromDTO(foroDTO, foro);
        ForosModel actualizado = forosRepository.save(foro);
        return foroMapper.toResponseDTO(actualizado);
    }

    @Override
    public String eliminarForo(ObjectId id) {
        if (!forosRepository.existsById(id)) {
            throw new RuntimeException("No se encontró foro con ID: " + id);
        }
        forosRepository.deleteById(id);
        return "Foro eliminado correctamente con ID: " + id;
    }
}
