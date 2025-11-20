package com.backend.sigclc.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.DTO.Estadisticas.LectorActivoResponseDTO;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;

@Repository
public interface IUsuariosRepository extends MongoRepository<UsuariosModel, ObjectId> {
    //* Consultas */

    // Buscar usuario por correo electrónico usando aggregation
    @Query("{ 'correoElectronico': ?0 }")
    Optional<UsuariosModel> findByCorreoElectronicoAgg(String correoElectronico);

    // Buscar usuarios por rol usando aggregation
    @Query("{ 'rolUsuario': ?0 }")
    List<UsuariosModel> findByRolUsuarioAgg(String rolUsuario);

    //* Estadísticas del Sistema */
    @Aggregation(pipeline = {
        // 0. Filtrar solo lectores
        "{ $match: { rol: 'lector' } }",

        // 1. PROPIETARIO → Propuestas creadas este mes
        "{ $lookup: {" +
            "from: 'propuestasLibros'," +
            "let: { userId: '$_id' }," +
            "pipeline: [" +
                "{ $match: { $expr: { $eq: ['$usuarioProponente.usuarioId', '$$userId'] } } }," +
                "{ $match: { $expr: {" +
                    "$and: [" +
                        "{ $eq: [{ $month: '$fechaPropuesta' }, { $month: new Date() }] }," +
                        "{ $eq: [{ $year: '$fechaPropuesta' }, { $year: new Date() }] }" +
                    "]" +
                "} } }" +
            "]," +
            "as: 'propuestasCreadas'" +
        "} }",

        // 2. VOTOS realizados este mes
        "{ $lookup: {" +
            "from: 'propuestasLibros'," +
            "let: { userId: '$_id' }," +
            "pipeline: [" +
                "{ $unwind: '$votos' }," +
                "{ $match: { $expr: { $eq: ['$votos.usuarioId', '$$userId'] } } }," +
                "{ $match: { $expr: {" +
                    "$and: [" +
                        "{ $eq: [{ $month: '$votos.fechaVoto' }, { $month: new Date() }] }," +
                        "{ $eq: [{ $year: '$votos.fechaVoto' }, { $year: new Date() }] }" +
                    "]" +
                "} } }" +
            "]," +
            "as: 'votosRealizados'" +
        "} }",

        // 3. Reseñas creadas este mes
        "{ $lookup: {" +
            "from: 'resenias'," +
            "let: { userId: '$_id' }," +
            "pipeline: [" +
                "{ $match: { $expr: { $eq: ['$redactor.usuarioId', '$$userId'] } } }," +
                "{ $match: { $expr: {" +
                    "$and: [" +
                        "{ $eq: [{ $month: '$fechaPublicacion' }, { $month: new Date() }] }," +
                        "{ $eq: [{ $year: '$fechaPublicacion' }, { $year: new Date() }] }" +
                    "]" +
                "} } }" +
            "]," +
            "as: 'reseniasCreadas'" +
        "} }",

        // 4. Comentarios de reseñas hechos este mes
        "{ $lookup: {" +
            "from: 'resenias'," +
            "let: { userId: '$_id' }," +
            "pipeline: [" +
                "{ $unwind: '$comentarios' }," +
                "{ $match: { $expr: { $eq: ['$comentarios.comentador.usuarioId', '$$userId'] } } }," +
                "{ $match: { $expr: {" +
                    "$and: [" +
                        "{ $eq: [{ $month: '$comentarios.fecha' }, { $month: new Date() }] }," +
                        "{ $eq: [{ $year: '$comentarios.fecha' }, { $year: new Date() }] }" +
                    "]" +
                "} } }" +
            "]," +
            "as: 'comentariosResenias'" +
        "} }",

        // 5. Comentarios en foros este mes
        "{ $lookup: {" +
            "from: 'comentariosForos'," +
            "let: { userId: '$_id' }," +
            "pipeline: [" +
                "{ $match: { $expr: { $eq: ['$redactor.usuarioId', '$$userId'] } } }," +
                "{ $match: { $expr: {" +
                    "$and: [" +
                        "{ $eq: [{ $month: '$fechaPublicacion' }, { $month: new Date() }] }," +
                        "{ $eq: [{ $year: '$fechaPublicacion' }, { $year: new Date() }] }" +
                    "]" +
                "} } }" +
            "]," +
            "as: 'comentariosForos'" +
        "} }",

        // 6. Inscripciones en retos activos este mes
        "{ $lookup: {" +
            "from: 'retosLectura'," +
            "let: { userId: '$_id' }," +
            "pipeline: [" +
                "{ $match: { $expr: {" +
                    "$and: [" +
                        "{ $lte: ['$fechaInicio', new Date()] }," +
                        "{ $gte: ['$fechaFinalizacion', new Date()] }" +
                    "]" +
                "} } }," +
                "{ $unwind: '$usuariosInscritos' }," +
                "{ $match: { $expr: { $eq: ['$usuariosInscritos.usuarioId', '$$userId'] } } }" +
            "]," +
            "as: 'inscripcionesRetos'" +
        "} }",

        // PROYECCIÓN FINAL
        "{ $project: {" +
            "nombreCompleto: 1," +
            "propuestasCreadas: { $size: '$propuestasCreadas' }," +
            "votosRealizados: { $size: '$votosRealizados' }," +
            "reseniasCreadas: { $size: '$reseniasCreadas' }," +
            "comentariosResenias: { $size: '$comentariosResenias' }," +
            "comentariosForos: { $size: '$comentariosForos' }," +
            "inscripcionesRetos: { $size: '$inscripcionesRetos' }," +
            "actividadTotal: {" +
                "$add: [" +
                    "{ $size: '$propuestasCreadas' }," +
                    "{ $size: '$votosRealizados' }," +
                    "{ $size: '$reseniasCreadas' }," +
                    "{ $size: '$comentariosResenias' }," +
                    "{ $size: '$comentariosForos' }," +
                    "{ $size: '$inscripcionesRetos' }" +
                "]" +
            "}" +
        "} }",

        // ORDENAR por el total de actividad
        "{ $sort: { actividadTotal: -1 } }",

        // LIMITAR a los 5 lectores más activos
        "{ $limit: 5 }"
    })
    List<LectorActivoResponseDTO> lectoresMasActivosMensual();
}
