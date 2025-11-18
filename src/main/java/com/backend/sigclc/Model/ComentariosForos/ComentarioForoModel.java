package com.backend.sigclc.Model.ComentariosForos;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document ("comentariosForos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioForoModel {
    @Id
    private ObjectId id;
    private ObjectId foroId;
    private RedactorModel redactor;
    private Date fechaPublicacion;
    private String comentario;
    private ObjectId parentId;

    @JsonProperty("id")
    public String getIdAString(){
        return id != null ? id.toHexString():null;
    }

    @JsonProperty("foroId")
    public String getForoIdAString(){
        return foroId != null ? foroId.toHexString():null;
    }

    @JsonProperty("parentId")
    public String getParentIdAsString(){
        return parentId != null ? parentId.toHexString():null;
    }
}
