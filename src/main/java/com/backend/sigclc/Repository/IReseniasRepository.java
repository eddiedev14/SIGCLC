package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.sigclc.Model.Resenias.ComentarioModel;
import com.backend.sigclc.Model.Resenias.ReseniaModel;
import com.backend.sigclc.Model.Resenias.ValoracionModel;

public interface IReseniasRepository extends MongoRepository <ReseniaModel, ObjectId> {
    //* Consultas básicas */

    // Consultas por redactor
    @Query("{'redactor.usuarioId': ?0}")
    List<ReseniaModel> buscarReseniasPorRedactor(ObjectId redactorId);

    // Consultas por libro
    @Query("{'libro.libroId': ?0}")
    List<ReseniaModel> buscarReseniasPorLibro(ObjectId libroId);

    // * Agregaciones

    // Buscar comentarios de un usuario
    @Aggregation(pipeline = {
        //* Filtra documentos que tengan comentarios del usuario */
        "{ $match: { 'comentarios.comentador.usuarioId': ?0 } }",
        //* Descompone el array de comentarios */
        "{ $unwind: '$comentarios' }",
        //* Filtra solo los comentarios del usuario */
        "{ $match: { 'comentarios.comentador.usuarioId': ?0 } }",
        //* Devuelve solo el comentario, no el documento completo */
        "{ $replaceRoot: { newRoot: '$comentarios' } }"
    })
    List<ComentarioModel> buscarComentariosDeUsuario(ObjectId usuarioId);

    // Buscar valoraciones de un usuario
    @Aggregation(pipeline = {
        //* Filtra documentos que tengan valoraciones del usuario */
        "{ $match: { 'valoraciones.valorador.usuarioId': ?0 } }",
        //* Descompone el array de valoraciones */
        "{ $unwind: '$valoraciones' }",
        //* Filtra solo las valoraciones del usuario */
        "{ $match: { 'valoraciones.valorador.usuarioId': ?0 } }",
        //* Devuelve solo la valoración, no el documento completo */
        "{ $replaceRoot: { newRoot: '$valoraciones' } }"
    })
    List<ValoracionModel> buscarValoracionesDeUsuario(ObjectId usuarioId);
}
