package com.backend.sigclc.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.Reuniones.ReunionesModel;

@Repository
public interface IReunionesRepository extends MongoRepository <ReunionesModel, ObjectId>{
    
}
