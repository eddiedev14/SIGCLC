package com.backend.sigclc.Service.Resenias;

import com.backend.sigclc.DTO.Resenias.ReseniaCreateDTO;
import com.backend.sigclc.DTO.Resenias.ReseniaResponseDTO;

public interface IReseniasService {
    public ReseniaResponseDTO crearResenia(ReseniaCreateDTO reseniaCreateDTO);
}
