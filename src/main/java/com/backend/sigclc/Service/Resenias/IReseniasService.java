package com.backend.sigclc.Service.Resenias;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioCreateDTO;
import com.backend.sigclc.DTO.Resenias.Comentario.ComentarioUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionCreateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionUpdateDTO;

public interface IReseniasService {
    public ReseniaResponseDTO crearResenia(ReseniaCreateDTO reseniaCreateDTO);
    public List<ReseniaResponseDTO> listarResenias();
    public ReseniaResponseDTO buscarReseniaPorId(ObjectId id);
    public List<ReseniaResponseDTO> buscarReseniasPorRedactor(ObjectId redactorId);
    public List<ReseniaResponseDTO> buscarReseniasPorLibro(ObjectId libroId);
    public ReseniaResponseDTO actualizarResenia(ObjectId reseniaId, ReseniaUpdateDTO reseniaUpdateDTO);
    public ReseniaResponseDTO valorarResenia(ObjectId reseniaId, ValoracionCreateDTO valoracionCreateDTO);
    public ReseniaResponseDTO comentarResenia(ObjectId reseniaId, ComentarioCreateDTO comentarioCreateDTO);
    public ReseniaResponseDTO agregarArchivosAResenia(ObjectId reseniaId, List<MultipartFile> archivosAdjuntos);
    public ReseniaResponseDTO actualizarValoracion(ObjectId reseniaId, ObjectId usuarioId, ValoracionUpdateDTO valoracionUpdateDTO);
    public ReseniaResponseDTO actualizarComentario(ObjectId reseniaId, ObjectId comentarioId, ComentarioUpdateDTO comentarioUpdateDTO);
    public ReseniaResponseDTO eliminarValoracion(ObjectId reseniaId, ObjectId usuarioId);
    public ReseniaResponseDTO eliminarComentario(ObjectId reseniaId, ObjectId comentarioId);
    public ReseniaResponseDTO eliminarArchivosDeResenia(ObjectId reseniaId, List<String> archivoUuids);
    public String eliminarResenia(ObjectId reseniaId);
}
