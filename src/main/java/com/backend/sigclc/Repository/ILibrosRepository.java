package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.backend.sigclc.DTO.Estadisticas.GeneroPopularResponseDTO;
import com.backend.sigclc.DTO.Estadisticas.LibroLeidoResponseDTO;
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

    //* Estadisticas */

    // Libros mas leidos
    @Aggregation(pipeline = {
        // 1. Lookup de propuestas seleccionadas y leídas este mes
        "{ $lookup: {" +
            "from: 'propuestasLibros'," +
            "localField: '_id'," +
            "foreignField: 'libroPropuesto.libroId'," +
            "as: 'propuestas'" +
        "} }",

        // Filtrar solo propuestas seleccionadas/leídas este mes
        "{ $addFields: {" +
            "propuestas: {" +
                "$filter: {" +
                    "input: '$propuestas'," +
                    "as: 'p'," +
                    "cond: {" +
                        "$and: [" +
                            "{ $eq: ['$$p.estadoPropuesta', 'seleccionada'] }," +
                            "{ $eq: ['$$p.libroPropuesto.estadoLectura', 'leido'] }," +
                            "{ $eq: [{ $month: '$$p.periodoSeleccion.fechaFin' }, { $month: new Date() }] }," +
                            "{ $eq: [{ $year: '$$p.periodoSeleccion.fechaFin' }, { $year: new Date() }] }" +
                        "]" +
                    "}" +
                "}" +
            "}" +
        "} }",

        // 2. Lookup de retos que incluyen el libro
        "{ $lookup: {" +
            "from: 'retosLectura'," +
            "let: { libroId: '$_id' }," +
            "pipeline: [" +
                // Incluir solo retos donde el libro está dentro de librosAsociados
                "{ $match: { $expr: { $in: ['$$libroId', '$librosAsociados.libroId'] } } }," +

                // Reto ya finalizado (solo cuenta si ya acabó)
                "{ $match: { $expr: { $lt: ['$fechaFinalizacion', new Date()] } } }," +

                // Reto terminó este mes
                "{ $match: { $expr: { $and: [" +
                    "{ $eq: [{ $month: '$fechaFinalizacion' }, { $month: new Date() }] }," +
                    "{ $eq: [{ $year: '$fechaFinalizacion' }, { $year: new Date() }] }" +
                "] } } }," +
                "{ $match: { $expr: { $gt: [ { $size: '$usuariosInscritos' }, 1 ] } } }" +
            "]," +
            "as: 'retos'" +
        "} }",

        // 3. Crear indicadores y sumatoria total
        "{ $project: {" +
            "titulo: 1," +
            "generos: 1," +
            "lecturasPropuestas: { $size: '$propuestas' }," +
            "lecturasRetos: { $size: '$retos' }," +
            "totalLecturas: { $add: [" +
                "{ $size: '$propuestas' }," +
                "{ $size: '$retos' }" +
            "] }" +
        "} }",

        // 4. Orden por libros más leídos
        "{ $sort: { totalLecturas: -1 } }",

        // 5. Limitar a los 5 libros más leídos
        "{ $limit: 5 }"
    })
    List<LibroLeidoResponseDTO> librosMasLeidosMensual();

    @Aggregation(pipeline = {
        // 1. Desanida los géneros del documento principal (Libros)
        "{ $unwind: '$generos' }",

        // 2. Agrupa por género. Esto crea un documento ÚNICO por cada género, resolviendo la duplicación.
        "{ $group: { _id: '$generos', libroIds: { $addToSet: '$_id' } } }",

        // 3. Busca reuniones
        "{ $lookup: { " +
            "from: 'reuniones', " +
            "let: { genero: '$_id' }, " +
            "pipeline: [" +
                // Filtra solo reuniones del mes y año actual
                "{ $match: { $expr: { $and: [" +
                    "{ $eq: [ { $month: '$fecha' }, { $month: new Date() } ] }," +
                    "{ $eq: [ { $year: '$fecha' }, { $year: new Date() } ] }" +
                "] } } }," +
                // Desanida los libros de la reunión
                "{ $unwind: '$librosSeleccionados' }," +
                // Filtra si el género actual está en el array de géneros de ese libro
                "{ $match: { $expr: { $in: ['$$genero', '$librosSeleccionados.generos'] } } }," +
                // De-duplica: agrupa por el _id de la reunión para contarla una sola vez
                "{ $group: { _id: '$_id' } }" +
            "], " +
            "as: 'reunionesConGenero' " +
        "} }",

        // 4. Busca retos de lectura
        "{ $lookup: { " +
            "from: 'retosLectura', " +
            "let: { genero: '$_id' }, " +
            "pipeline: [" +
                // Filtra solo retos iniciados en el mes y año actual
                "{ $match: { $expr: { $and: [" +
                    "{ $eq: [ { $month: '$fechaInicio' }, { $month: new Date() } ] }," +
                    "{ $eq: [ { $year: '$fechaInicio' }, { $year: new Date() } ] }" +
                "] } } }," +
                // Desanida los libros del reto
                "{ $unwind: '$librosAsociados' }," +
                // Filtra si el género actual está en el array de géneros de ese libro
                "{ $match: { $expr: { $in: ['$$genero', '$librosAsociados.generos'] } } }," +
                // De-duplica: agrupa por el _id del reto para contarlo una sola vez
                "{ $group: { _id: '$_id' } }" +
            "], " +
            "as: 'retosConGenero' " +
        "} }",

        // 5. Busca resenias
        "{ $lookup: { " +
            "from: 'resenias', " +
            "let: { libroIds: '$libroIds' }, " +
            "pipeline: [" +
                // Filtra si el ID del libro en la reseña ('$libro.libroId') está en la lista de IDs de libros de este género
                "{ $match: { $expr: { $in: ['$libro.libroId', '$$libroIds'] } } }" +
            "], " +
            "as: 'reseniasConGenero' " +
        "} }",

        // 6. Busca foros
        "{ $lookup: { " +
            "from: 'foros', " +
            "let: { genero: '$_id' }, " +
            "pipeline: [" +
                // Filtra si tipoTematica es "genero"
                "{ $match: { $expr: { $and: [ { $eq: ['genero', '$tipoTematica'] }, { $eq: ['$$genero', '$tematica'] } ] } } }" +
            "] " +
            "as: 'forosConGenero' " +
        "} }",

        // 7. Buscar comentarios de esos foros en la coleccion comentariosForos
        "{ $lookup: { " +
            "from: 'comentariosForos', " +
            "let: { forosIds: '$forosConGenero._id' }, " +
            "pipeline: [" +
                "{ $match: { $expr: { $in: ['$foroId', '$$forosIds'] } } }" +
            "] " +
            "as: 'comentariosForosConGenero' " +
        "} }",

        // 8. Proyecta y calcula el total
        "{ $project: { " +
            "_id: 0, " +
            "genero: '$_id', " +
            "reuniones: { $size: '$reunionesConGenero' }, " +
            "retos: { $size: '$retosConGenero' }, " +
            "resenias: { $size: '$reseniasConGenero' }, " +
            "comentariosResenias: { $size: '$reseniasConGenero.comentarios' }, " +
            "foros: { $size: '$forosConGenero' }, " +
            "comentariosForos: { $size: '$comentariosForosConGenero' }, " +
            "popularidadTotal: { $add: [ { $size: '$reunionesConGenero' }, { $size: '$retosConGenero' }, { $size: '$reseniasConGenero' }, { $size: '$reseniasConGenero.comentarios' }, { $size: '$forosConGenero' }, { $size: '$comentariosForosConGenero' } ] }" +
        "} }",

        // 9. Formato del DTO
        "{ $project: { " +
            "genero: 1, " +
            "reuniones: 1, " +
            "retos: 1, " +
            "resenias: 1, " +
            "comentariosResenias: 1, " +
            "foros: 1, " +
            "comentariosForos: 1, " +
            "popularidadTotal: 1, " +
        "} }",

        // 10. Ordenar y limitar
        "{ $sort: { popularidadTotal: -1 } }",
        "{ $limit: 5 }"
    })
    List<GeneroPopularResponseDTO> generosMasPopularesDelMes();
}