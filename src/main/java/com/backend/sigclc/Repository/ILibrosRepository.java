package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

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
}
