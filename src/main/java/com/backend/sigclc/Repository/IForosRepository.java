package com.backend.sigclc.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.Foros.ForosModel;
import com.backend.sigclc.Model.Foros.TipoTematica;

@Repository
public interface IForosRepository extends MongoRepository<ForosModel, ObjectId> {

    // Buscar foro por nombre de temática
    @Aggregation(pipeline = {
        "{ $match: { nombreTematica: ?0 } }",
        "{ $project: { _id: 1, tipoTematica: 1, nombreTematica: 1, fechaPublicacion: 1, moderador: 1 } }"
    })
    Optional<ForosModel> buscarPorNombreTematica(String nombreTematica);

    // Buscar foros por tipo de temática (género, autor o tema)
    @Aggregation(pipeline = {
        "{ $match: { tipoTematica: ?0 } }",
        "{ $project: { _id: 1, tipoTematica: 1, nombreTematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> buscarPorTipoTematica(TipoTematica tipoTematica);

    // Buscar foros por ID del moderador
    @Aggregation(pipeline = {
        "{ $match: { 'moderador.moderadorId': ?0 } }",
        "{ $project: { _id: 1, tipoTematica: 1, nombreTematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> buscarPorModerador(ObjectId moderadorId);

    // Buscar foros publicados después de una fecha específica
    @Aggregation(pipeline = {
        "{ $match: { fechaPublicacion: { $gt: ?0 } } }",
        "{ $project: { _id: 1, tipoTematica: 1, nombreTematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: 1 } }"
    })
    List<ForosModel> buscarForosPublicadosDespues(Date fecha);

    // Buscar foros publicados antes de una fecha específica
    @Aggregation(pipeline = {
        "{ $match: { fechaPublicacion: { $lt: ?0 } } }",
        "{ $project: { _id: 1, tipoTematica: 1, nombreTematica: 1, fechaPublicacion: 1, moderador: 1 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> buscarForosPublicadosAntes(Date fecha);
}
