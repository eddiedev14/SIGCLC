package com.backend.sigclc.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.sigclc.Model.Resenias.ReseniaModel;

public interface IReseniasRepository extends MongoRepository <ReseniaModel, ObjectId> {
    
}
