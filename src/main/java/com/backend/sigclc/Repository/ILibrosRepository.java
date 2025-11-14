package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.Libros.LibrosModel;

public interface ILibrosRepository extends MongoRepository <LibrosModel, ObjectId> {
    
    // Actualizar nombre del creador en caso de que el nombre del usuario se modifique
    @Query("{ 'creador.usuarioId': ?0 }") // Filtro para buscar libros por usuarioId
    @Update("{ '$set': { 'creador.nombreCompleto': ?1 } }") // Actualizar el nombre del creador
    void actualizarNombreCreador(ObjectId usuarioId, String nuevoNombre);

    // Validar si el usuario está asociado como creador de un libro
    @ExistsQuery("{ 'creador.usuarioId': ?0 }")
    boolean existsByCreadorId(ObjectId usuarioId);

    // Buscar libros por genero
    @Aggregation(pipeline = {
        "{ $match: { generos: { $elemMatch: { $eq: ?0 } } } }",
        "{ $sort: { titulo: 1 } }"
    })
    List<LibrosModel> buscarPorGenero(GeneroLibro genero);

    // Listar libros por autor
    @Aggregation(pipeline = {
    "{ $match: { autores: { $elemMatch: { $eq: ?0 } } } }",
    "{ $sort: { titulo: 1 } }"
    })
    List<LibrosModel> buscarPorAutor(String autor);
}
