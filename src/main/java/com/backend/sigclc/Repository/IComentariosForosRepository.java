package com.backend.sigclc.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.backend.sigclc.Model.ComentariosForos.ComentarioForoModel;

public interface IComentariosForosRepository extends MongoRepository<ComentarioForoModel, ObjectId> {

    // Actualizar nombre de usuario si se modifica
    @Query("{'redactor.usuarioId': ?0 }")
    @Update("{ '$set': { 'redactor.nombreCompleto': ?1 } }")
    void actualizarNombreRedactor(ObjectId usuarioId, String nombreCompleto);

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

    // Comentarios raíz de un foro (parentId == null o no existe)
    @Aggregation(pipeline = {
        "{ '$match': { " +
            "'foroId': ?0, " +
            "'$or': [ " +
                "{ 'parentId': null }, " +
                "{ 'parentId': { '$exists': false } } " +
            "] " +
        "} }",
        "{ '$sort': { 'fechaPublicacion': 1 } }"
    })
    List<ComentarioForoModel> buscarComentariosPadrePorForoId(ObjectId foroId);

    // Comentarios realizados por un usuario concreto
    @Aggregation(pipeline = {
        "{ '$match': { 'redactor.usuarioId': ?0 } }",
        "{ '$sort': { 'fechaPublicacion': 1 } }"
    })
    List<ComentarioForoModel> buscarComentariosPorUsuario(ObjectId usuarioId);

    // Buscar el comentario padre de un comentario concreto
    @Aggregation(pipeline = {
        "{ '$match': { '_id': ?0 } }",
        "{ '$lookup': { " +
            "'from': 'comentariosForos', " +
            "'localField': 'parentId', " +
            "'foreignField': '_id', " +
            "'as': 'comentarioPadre' " +
        "} }",
        "{ '$unwind': '$comentarioPadre' }",
        "{ '$replaceRoot': { 'newRoot': '$comentarioPadre' } }"
    })
    Optional<ComentarioForoModel> buscarComentarioPadre(ObjectId comentarioId);


}