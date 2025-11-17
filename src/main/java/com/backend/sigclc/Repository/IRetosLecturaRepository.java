package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.RetosLectura.RetosLecturaModel;

public interface IRetosLecturaRepository extends MongoRepository <RetosLecturaModel, ObjectId>{
    // Actualizar nombre de un usuario inscrito en un reto
    @Query("{ 'usuariosInscritos.usuarioId': ?0 }")
    @Update("{$set: { 'usuariosInscritos.$.nombreCompleto': ?1 }}")
    void actualizarNombreUsuarioInscrito(ObjectId usuarioId, String nombreCompleto);

    // Actualizar título de un libro dentro de un reto
    @Query("{ 'librosAsociados.libroId': ?0 }")
    @Update("{$set: { 'librosAsociados.$.titulo': ?1 }}")
    void actualizarTituloLibroAsociado(ObjectId libroId, String nuevoTitulo);

    // Actualizar generos de un libro dentro de un reto
    @Query("{ 'librosAsociados.libroId': ?0 }")
    @Update("{$set: { 'librosAsociados.$.generos': ?1 }}")
    void actualizarGenerosLibro(ObjectId libroId, List<GeneroLibro> generos);

    // Validar si un libro está asociado a algún reto
    // Se usará para validar la eliminación de los libros
    @ExistsQuery("{ 'librosAsociados.libroId': ?0 }")
    boolean existsByLibroAsociado(ObjectId libroId);

    // Validar si un usuario está inscrito en algún reto
    // Se usará para validar la eliminación de los usuarios
    @ExistsQuery("{ 'usuariosInscritos.usuarioId': ?0 }")
    boolean existsByUsuarioInscrito(ObjectId usuarioId);

    @Query(value = """
    {
    '_id': ?0,
    'usuariosInscritos.usuarioId': ?1,
    'usuariosInscritos.progreso.libroAsociadoId': ?2
    }
    """,
    fields = """
    {
    'usuariosInscritos.$': 1
    }""")
    RetosLecturaModel buscarProgreso(ObjectId retoId, ObjectId usuarioId, ObjectId libroAsociadoId);

    //--------Consultas-----------

    // Buscar retos donde esté inscrito un usuario
    @Query("{ 'usuariosInscritos.usuarioId': ?0 }")
    List<RetosLecturaModel> buscarRetosPorUsuario(ObjectId usuarioId);

    // Buscar retos que incluyan un libro asociado
    @Query("{ 'librosAsociados.libroId': ?0 }")
    List<RetosLecturaModel> buscarRetosPorLibro(ObjectId libroId);

    // Buscar retos activos según la fecha actual
    @Query("{ 'fechaInicio': { $lte: ?0 }, 'fechaFinalizacion': { $gte: ?0 } }")
    List<RetosLecturaModel> buscarRetosActivos(java.util.Date hoy);
}
