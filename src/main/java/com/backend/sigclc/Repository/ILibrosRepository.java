package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.Libros.LibrosModel;

public interface ILibrosRepository extends MongoRepository <LibrosModel, ObjectId> {
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
