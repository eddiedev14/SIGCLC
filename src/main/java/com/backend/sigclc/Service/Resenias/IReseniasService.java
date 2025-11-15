package com.backend.sigclc.Service.Resenias;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaUpdateDTO;
import com.backend.sigclc.DTO.Resenias.Valoracion.ValoracionCreateDTO;

public interface IReseniasService {
    public ReseniaResponseDTO crearResenia(ReseniaCreateDTO reseniaCreateDTO);
    public List<ReseniaResponseDTO> listarResenias();
    public ReseniaResponseDTO buscarReseniaPorId(ObjectId id);
    public List<ReseniaResponseDTO> buscarReseniasPorRedactor(ObjectId redactorId);
    public List<ReseniaResponseDTO> buscarReseniasPorLibro(ObjectId libroId);
    public ReseniaResponseDTO actualizarResenia(ObjectId reseniaId, ReseniaUpdateDTO reseniaUpdateDTO);
    public ReseniaResponseDTO valorarResenia(ObjectId reseniaId, ValoracionCreateDTO valoracionCreateDTO);
}
