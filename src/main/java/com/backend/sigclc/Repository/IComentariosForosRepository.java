package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.sigclc.Model.ComentariosForos.ComentarioForoModel;

public interface IComentariosForosRepository extends MongoRepository<ComentarioForoModel, ObjectId> {

    // Comentarios por foro (ordenados por fechaPublicacion asc)
    @Aggregation(pipeline = {
        "{ '$match': { 'foroId': ?0 } }",
        "{ '$sort': { 'fechaPublicacion': 1 } }"
    })
    List<ComentarioForoModel> buscarPorForoId(ObjectId foroId);

    // Comentarios por parentId (replies de un comentario)
    @Aggregation(pipeline = {
        "{ '$match': { 'parentId': ?0 } }",
        "{ '$sort': { 'fechaPublicacion': 1 } }"
    })
    List<ComentarioForoModel> buscarPorParentId(ObjectId parentId);

}