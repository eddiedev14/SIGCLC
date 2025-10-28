package com.backend.sigclc.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.sigclc.Model.Libros.LibrosModel;

public interface ILibrosRepository extends MongoRepository <LibrosModel, ObjectId> {
    
}
