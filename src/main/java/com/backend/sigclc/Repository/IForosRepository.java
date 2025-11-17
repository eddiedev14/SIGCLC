package com.backend.sigclc.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.Foros.ForosModel;
import com.backend.sigclc.Model.Foros.TipoTematica;

@Repository
public interface IForosRepository extends MongoRepository<ForosModel, ObjectId> {
    // Actualizar nombre de usuario si se modifica
    @Query("{'moderador.moderadorId': ?0 }")
    @Update("{ '$set': { 'moderador.nombreCompleto': ?1 } }")
    void actualizarNombreModerador(ObjectId usuarioId, String nuevoNombre);

    // Validar si el usuario está asociado como moderador de un foro
    @ExistsQuery("{'moderador.moderadorId': ?0 }")
    boolean existsByModeradorId(ObjectId usuarioId);

    //* Agregaciones */


    //Buscar foro por título
    @Aggregation(pipeline = {
        "{ $match: { titulo: ?0 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> listarPorTitulo(String titulo);

    // Buscar foro por nombre de temática
    @Aggregation(pipeline = {
        "{ $match: { tematica: ?0 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> listarPorTematica(String tematica);

    // Buscar foros por tipo de temática (género, autor o tema)
    @Aggregation(pipeline = {
        "{ $match: { tipoTematica: ?0 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> listarPorTipoTematica(TipoTematica tipoTematica);

    // Buscar foros por ID del moderador
    @Aggregation(pipeline = {
        "{ $match: { 'moderador.moderadorId': ?0 } }",
        "{ $sort: { fechaPublicacion: -1 } }"
    })
    List<ForosModel> listarPorModerador(ObjectId moderadorId);

    // Actualizar nombre de usuario si se modifica

    @Query("{'moderador.moderadorId': ?0 }")
    @Update("{ '$set': { 'moderador.nombreCompleto': ?1 } }")
    void actualizarNombreModerador(ObjectId usuarioId, String nuevoNombre);

    List<ForosModel> buscarPorModerador(ObjectId moderadorId);
}
