package com.backend.sigclc.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.sigclc.Model.Resenias.ReseniaModel;

public interface IReseniasRepository extends MongoRepository <ReseniaModel, ObjectId> {
    //* Consultas básicas */

    // Consultas por redactor
    @Query("{'redactor.usuarioId': ?0}")
    List<ReseniaModel> buscarReseniasPorRedactor(ObjectId redactorId);
}
