package com.backend.sigclc.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.PropuestasLibros.PropuestasLibrosModel;
import org.bson.types.ObjectId;

@Repository
public interface IPropuestasLibrosRepository extends MongoRepository<PropuestasLibrosModel, ObjectId> {
    //* Agregaciones a futuro */
}
