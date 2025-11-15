package com.backend.sigclc.Service.Resenias;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;

public interface IReseniasService {
    public ReseniaResponseDTO crearResenia(ReseniaCreateDTO reseniaCreateDTO);
    public List<ReseniaResponseDTO> listarResenias();
    public ReseniaResponseDTO buscarReseniaPorId(ObjectId id);
}
