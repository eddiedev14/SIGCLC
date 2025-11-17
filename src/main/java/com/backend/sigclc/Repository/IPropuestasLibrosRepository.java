package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;
import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;

@Repository
public interface IPropuestasLibrosRepository extends MongoRepository<PropuestasLibrosModel, ObjectId> {
    // Actualizar titulo del libro propuesto si se modifica
    @Query("{ 'libroPropuesto.libroId': ?0 }")
    @Update("{ '$set': { 'libroPropuesto.titulo': ?1 } }")
    void actualizarTituloLibroPropuesto(ObjectId libroId, String nuevoTitulo);

    // Actualizar generos del libro propuesto si se modifican
    @Query("{ 'libroPropuesto.libroId': ?0 }")
    @Update("{ '$set': { 'libroPropuesto.generos': ?1 } }")
    void actualizarGenerosLibroPropuesto(ObjectId libroId, List<GeneroLibro> nuevoGenero);

    // Actualizar nombre de usuario (Tanto en usuarioProponente como en votos) si se modifica
    @Query("{ 'usuarioProponente.usuarioId': ?0 }")
    @Update("{ '$set': { 'usuarioProponente.nombreCompleto': ?1 } }")
    void actualizarNombreUsuarioProponente(ObjectId usuarioId, String nuevoNombre);
    
    @Query("{ 'votos.usuarioId': ?0 }")
    @Update("{ '$set': { 'votos.$.nombreCompleto': ?1 } }")
    void actualizarNombreUsuarioVoto(ObjectId usuarioId, String nuevoNombre);

    // Validar si el usuario está asociado como proponente de una propuesta de libro
    @ExistsQuery("{ 'usuarioProponente.usuarioId': ?0 }")
    boolean existsByProponenteId(ObjectId usuarioId);

    // Validar si el usuario está asociado como votante de una propuesta de libro
    @ExistsQuery("{ 'votos.usuarioId': ?0 }")
    boolean existsByVotanteId(ObjectId usuarioId);

    // Validar si el libro está asociado a una propuesta
    @ExistsQuery("{ 'libroPropuesto.libroId': ?0 }")
    boolean existsByLibroPropuestoLibroId(ObjectId libroId);

    //* Consultas */

    // Buscar propuestas por estado
    @Query("{ 'estadoPropuesta': ?0 }")
    List<PropuestasLibrosModel> buscarPropuestasPorEstado(EstadoPropuesta estado);

    // Buscar propuestas por usuario
    @Query("{ 'usuarioProponente.usuarioId': ?0 }")
    List<PropuestasLibrosModel> buscarPropuestasPorUsuario(ObjectId usuarioId);

    // Buscar propuestas por libro
    @Query("{ 'libroPropuesto.libroId': ?0 }")
    List<PropuestasLibrosModel> buscarPropuestasPorLibro(ObjectId libroId);

    //* Agregaciones */

    // Retornar conteo de reuniones asociadas a una propuesta (en coleccion 'reuniones')
    @Aggregation(pipeline = {
        "{ $lookup: { from: 'reuniones', localField: '_id', foreignField: 'librosSeleccionados.propuestaId', as: 'reunionesAsociadas' } }",
        "{ $addFields: { tieneReunion: { $gt: [ { $size: '$reunionesAsociadas' }, 0 ] } } }",
        "{ $match: { _id: ?0 } }",
        "{ $project: { _id: 1, tieneReunion: 1 } }"
    })
    boolean tieneReuniones(ObjectId propuestaId);
}
