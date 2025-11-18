package com.backend.sigclc.Repository;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.backend.sigclc.Model.Resenias.ComentarioModel;
import com.backend.sigclc.Model.Resenias.ReseniaModel;
import com.backend.sigclc.Model.Resenias.ValoracionModel;

public interface IReseniasRepository extends MongoRepository <ReseniaModel, ObjectId> {
    //* Sincronización */
    // Actualizar nombre de usuario como redactor
    @Query("{'redactor.usuarioId': ?0}")
    @Update("{$set: {'redactor.nombreCompleto': ?1}}")
    void actualizarNombreRedactor(ObjectId usuarioId, String nombreCompleto);

    // Actualizar nombre de usuario como comentador
    @Query("{'comentarios.comentador.usuarioId': ?0}")
    @Update("{$set: {'comentarios.$.comentador.nombreCompleto': ?1}}")
    void actualizarNombreComentador(ObjectId usuarioId, String nombreCompleto);

    // Actualizar nombre de usuario como valorador
    @Query("{'valoraciones.valorador.usuarioId': ?0}")
    @Update("{$set: {'valoraciones.$.valorador.nombreCompleto': ?1}}")
    void actualizarNombreValorador(ObjectId usuarioId, String nombreCompleto);

    // Actualizar el titulo del libro reseniado
    @Query("{'libro.libroId': ?0}")
    @Update("{$set: {'libro.titulo': ?1}}")
    void actualizarTituloLibroReseniado(ObjectId libroId, String tituloLibro);

    // Validar si el usuario está asociado como redactor de una reseña
    @ExistsQuery("{'redactor.usuarioId': ?0}")
    boolean existsByRedactorId(ObjectId usuarioId);

    // Validar si el usuario está asociado como comentador de una reseña
    @ExistsQuery("{'comentarios.comentador.usuarioId': ?0}")
    boolean existsByComentadorId(ObjectId usuarioId);

    // Validar si el usuario está asociado como valorador de una reseña
    @ExistsQuery("{'valoraciones.valorador.usuarioId': ?0}")
    boolean existsByValoradorId(ObjectId usuarioId);

    // Validar si el libro está asociado como libro reseniado de una reseña
    @ExistsQuery("{'libro.libroId': ?0}")
    boolean existsByLibroId(ObjectId libroId);

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

    // Buscar reseñas del mes actual
    @Query("{ 'fechaPublicacion': { $gte: ?0, $lt: ?1 } }")
    List<ReseniaModel> buscarReseniasDelMesActual(Date start, Date end);
}
