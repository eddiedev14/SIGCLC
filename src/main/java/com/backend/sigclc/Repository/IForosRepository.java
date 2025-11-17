package com.backend.sigclc.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.Foros.ForosModel;
import com.backend.sigclc.Model.Foros.TipoTematica;

@Repository
public interface IForosRepository extends MongoRepository<ForosModel, ObjectId> {


    //Buscar foro por título
    @Aggregation(pipeline = {
        "{ $match: { titulo: ?0 } }",
        "{ $project: { _id: 1, tipoTematica: 1, tematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    Optional<ForosModel> listarPorTitulo(String titulo);

    // Buscar foro por nombre de temática
    @Aggregation(pipeline = {
        "{ $match: { tematica: ?0 } }",
        "{ $project: { _id: 1, tipoTematica: 1, tematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    Optional<ForosModel> listarPorTematica(String tematica);

    // Buscar foros por tipo de temática (género, autor o tema)
    @Aggregation(pipeline = {
        "{ $match: { tipoTematica: ?0 } }",
        "{ $project: { _id: 1, tipoTematica: 1, tematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> listarPorTipoTematica(TipoTematica tipoTematica);

    // Buscar foros por ID del moderador
    @Aggregation(pipeline = {
        "{ $match: { 'moderador.moderadorId': ?0 } }",
        "{ $project: { _id: 1, tipoTematica: 1, tematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> listarPorModerador(ObjectId moderadorId);

    // Actualizar nombre de usuario si se modifica

    @Query("{'moderador.moderadorId': ?0 }")
    @Update("{ '$set': { 'moderador.nombreCompleto': ?1 } }")
    void actualizarNombreModerador(ObjectId usuarioId, String nuevoNombre);

}
