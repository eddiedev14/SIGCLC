package com.backend.sigclc.Model.Foros;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ModeradorModel {
    private ObjectId moderadorId;
    private String nombreCompleto;

    public String getModeradorIdAsString(){
        return moderadorId.toHexString();
    }
}

