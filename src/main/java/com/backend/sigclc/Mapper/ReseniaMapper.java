package com.backend.sigclc.Mapper;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Resenias.LibroReseniado.LibroReseniadoCreateDTO;
import com.backend.sigclc.DTO.Resenias.LibroReseniado.LibroReseniadoResponseDTO;
import com.backend.sigclc.DTO.Resenias.Redactor.RedactorResponseDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionResponseDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoradorResponseDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentadorResponseDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioResponseDTO;
import com.backend.sigclc.Model.Resenias.ReseniaModel;
import com.backend.sigclc.Model.Resenias.ValoracionModel;
import com.backend.sigclc.Model.Resenias.ValoradorModel;
import com.backend.sigclc.Model.Resenias.RedactorModel;
import com.backend.sigclc.Model.Resenias.ComentadorModel;
import com.backend.sigclc.Model.Resenias.ComentarioModel;
import com.backend.sigclc.Model.Resenias.LibroReseniadoModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReseniaMapper {
    @Autowired
    private ArchivoAdjuntoMapper archivoAdjuntoMapper;
    
    //* Creación */
    public ReseniaModel toReseniaModel(ReseniaCreateDTO reseniaCreateDTO) {
        ReseniaModel reseniaModel = new ReseniaModel();
        reseniaModel.setRedactor(createRedactorModel(reseniaCreateDTO.getRedactorId()));
        reseniaModel.setLibro(createLibroReseniadoModel(reseniaCreateDTO.getLibroReseniado()));
        return reseniaModel;
    }

    public RedactorModel createRedactorModel(ObjectId redactorId) {
        RedactorModel redactorModel = new RedactorModel();
        redactorModel.setUsuarioId(redactorId);
        return redactorModel;
    }

    public LibroReseniadoModel createLibroReseniadoModel(LibroReseniadoCreateDTO libroReseniadoCreateDTO) {
        LibroReseniadoModel libroReseniadoModel = new LibroReseniadoModel();
        libroReseniadoModel.setLibroId(libroReseniadoCreateDTO.getLibroId());
        libroReseniadoModel.setCalificacion(libroReseniadoCreateDTO.getCalificacion());
        libroReseniadoModel.setOpinion(libroReseniadoCreateDTO.getOpinion());
        return libroReseniadoModel;
    }

    //* Responses */
    public ReseniaResponseDTO toResponseDTO(ReseniaModel reseniaModel) {
        ReseniaResponseDTO reseniaResponseDTO = new ReseniaResponseDTO();
        reseniaResponseDTO.setId(reseniaModel.getIdAString());
        reseniaResponseDTO.setRedactor(toRedactorResponseDTO(reseniaModel.getRedactor()));
        reseniaResponseDTO.setFechaPublicacion(reseniaModel.getFechaPublicacion());
        reseniaResponseDTO.setLibroReseniado(toLibroReseniadoResponseDTO(reseniaModel.getLibro()));
        reseniaResponseDTO.setArchivosAdjuntos(archivoAdjuntoMapper.toArchivosAdjuntosResponseDTOList(reseniaModel.getArchivosAdjuntos()));
        reseniaResponseDTO.setComentarios(toComentariosResponseDTOList(reseniaModel.getComentarios()));
        reseniaResponseDTO.setValoraciones(toValoracionesResponseDTOList(reseniaModel.getValoraciones()));
        return reseniaResponseDTO;
    }

    public RedactorResponseDTO toRedactorResponseDTO(RedactorModel redactorModel) {
        RedactorResponseDTO redactorResponseDTO = new RedactorResponseDTO();
        redactorResponseDTO.setUsuarioId(redactorModel.getUsuarioId().toHexString());
        redactorResponseDTO.setNombreCompleto(redactorModel.getNombreCompleto());
        return redactorResponseDTO;
    }

    public LibroReseniadoResponseDTO toLibroReseniadoResponseDTO(LibroReseniadoModel libroReseniadoModel) {
        LibroReseniadoResponseDTO libroReseniadoResponseDTO = new LibroReseniadoResponseDTO();
        libroReseniadoResponseDTO.setLibroId(libroReseniadoModel.getLibroId().toHexString());
        libroReseniadoResponseDTO.setTitulo(libroReseniadoModel.getTitulo());
        libroReseniadoResponseDTO.setCalificacion(libroReseniadoModel.getCalificacion());
        libroReseniadoResponseDTO.setOpinion(libroReseniadoModel.getOpinion());
        return libroReseniadoResponseDTO;
    }

    public ComentarioResponseDTO toComentarioResponseDTO(ComentarioModel comentarioModel) {
        ComentarioResponseDTO comentarioResponseDTO = new ComentarioResponseDTO();
        comentarioResponseDTO.setComentador(toComentadorResponseDTO(comentarioModel.getComentador()));
        comentarioResponseDTO.setFecha(comentarioModel.getFecha());
        comentarioResponseDTO.setComentario(comentarioModel.getComentario());
        return comentarioResponseDTO;
    }

    public ComentadorResponseDTO toComentadorResponseDTO(ComentadorModel comentadorModel) {
        ComentadorResponseDTO comentadorResponseDTO = new ComentadorResponseDTO();
        comentadorResponseDTO.setUsuarioId(comentadorModel.getComentadorIdAString());
        comentadorResponseDTO.setNombreCompleto(comentadorModel.getNombreCompleto());
        return comentadorResponseDTO;
    }

    public ValoracionResponseDTO toValoracionResponseDTO(ValoracionModel valoracionModel) {
        ValoracionResponseDTO valoracionResponseDTO = new ValoracionResponseDTO();
        valoracionResponseDTO.setValorador(toValoradorResponseDTO(valoracionModel.getValorador()));
        valoracionResponseDTO.setFecha(valoracionModel.getFecha());
        valoracionResponseDTO.setValoracion(valoracionModel.getValoracion());
        return valoracionResponseDTO;
    }

    public ValoradorResponseDTO toValoradorResponseDTO(ValoradorModel valoradorModel) {
        ValoradorResponseDTO valoradorResponseDTO = new ValoradorResponseDTO();
        valoradorResponseDTO.setUsuarioId(valoradorModel.getValoradorIdAString());
        valoradorResponseDTO.setNombreCompleto(valoradorModel.getNombreCompleto());
        return valoradorResponseDTO;
    }

    // Convertir a listas de responseDTO
    public List<ComentarioResponseDTO> toComentariosResponseDTOList(List<ComentarioModel> comentarios) {
        return comentarios.stream()
                .map(this::toComentarioResponseDTO)
                .toList();
    }

    public List<ValoracionResponseDTO> toValoracionesResponseDTOList(List<ValoracionModel> valoraciones) {
        return valoraciones.stream()
                .map(this::toValoracionResponseDTO)
                .toList();
    }
}
