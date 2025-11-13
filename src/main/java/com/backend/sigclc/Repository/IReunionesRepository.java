package com.backend.sigclc.Repository;



import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.Reuniones.ReunionesModel;

@Repository
public interface IReunionesRepository extends MongoRepository <ReunionesModel, ObjectId>{
    //Actualizar nombre de asistente si se modifica
    @Query("{'asistentes.id': ?0}")
    @Update("{'$set': {'asistentes.$.nombre': ?1}}")
    void actualizarNombreAsistente(ObjectId asistenteId, String nuevoNombre);

    // Actualizar título de libro si se modifica
    @Query("{'librosSeleccionados.id': ?0}")
    @Update("{'$set': {'librosSeleccionados.$.titulo': ?1}}")
    void actualizarTituloLibroSeleccionado(ObjectId libroId, String nuevoTitulo);

    // Actualizar autores del libro si se modifican
    @Query("{'librosSeleccionados.id': ?0}")
    @Update("{'$set': {'librosSeleccionados.$.autores': ?1}}")
    void actualizarAutoresLibroSeleccionado(ObjectId libroId, List<String> nuevosAutores);

    // Actualizar géneros del libro si se modifican
    @Query("{'librosSeleccionados.id': ?0}")
    @Update("{'$set': {'librosSeleccionados.$.generos': ?1}}")
    void actualizarGenerosLibroSeleccionado(ObjectId libroId, List<GeneroLibro> nuevosGeneros);

    // buscar reuniones por asistente
    @Query("{'asistentes.asistenteId': ?0}")
    List<ReunionesModel> buscarPorAsistenteId(ObjectId asistenteId);

    // buscar reuniones por libro seleccionado
    @Query("{'librosSeleccionados.propuestaId': ?0}")
    List<ReunionesModel> buscarPorLibroSeleccionadoId(ObjectId libroId);

    // buscar reuniones por fecha específica
    @Query("""
        {
            $expr: {
                $eq: [
                    { $dateToString: { format: "%Y-%m-%d", date: "$fecha" } },
                    { $dateToString: { format: "%Y-%m-%d", date: ?0 } }
                ]
            }
        }
        """)
    List<ReunionesModel> buscarPorFecha(Date fecha);

}
