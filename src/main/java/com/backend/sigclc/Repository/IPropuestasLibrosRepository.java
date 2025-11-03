package com.backend.sigclc.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.PropuestasLibros.EstadoPropuesta;
import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;
import com.backend.sigclc.Model.Libros.GeneroLibro;

import java.util.List;

import org.bson.types.ObjectId;

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
    @Update("{ '$set': { 'votos.$[voto].nombreCompleto': ?1 } }")
    void actualizarNombreUsuarioVoto(ObjectId usuarioId, String nuevoNombre);

    // Verificar si un libro tiene propuestas cuando el estado de la propuesta es en_votacion
    @Query(value = "{ 'libroPropuesto.libroId': ?0, 'estadoPropuesta': 'en_votacion' }", exists = true)
    boolean tienePropuestasEnVotacion(ObjectId libroId);
    
    //* Agregaciones */

    // Buscar propuestas por estado
    @Query("{ 'estadoPropuesta': ?0 }")
    List<PropuestasLibrosModel> buscarPropuestasPorEstado(EstadoPropuesta estado);

    // Buscar propuestas por usuario
    @Query("{ 'usuarioProponente.usuarioId': ?0 }")
    List<PropuestasLibrosModel> buscarPropuestasPorUsuario(ObjectId usuarioId);

    // Buscar propuestas por libro
    @Query("{ 'libroPropuesto.libroId': ?0 }")
    List<PropuestasLibrosModel> buscarPropuestasPorLibro(ObjectId libroId);
}
