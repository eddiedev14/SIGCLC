package com.backend.sigclc.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.backend.sigclc.Model.Usuarios.UsuariosModel;

@Repository
public interface IUsuariosRepository extends MongoRepository<UsuariosModel, ObjectId> {

    /**
     * 🔍 Buscar usuario por correo electrónico usando aggregation
     */
    @Aggregation(pipeline = {
        "{ $match: { correoElectronico: ?0 } }",
        "{ $project: { _id: 1, nombreCompleto: 1, edad: 1, ocupacion: 1, correoElectronico: 1, telefono: 1, rolUsuario: 1 } }"
    })
    Optional<UsuariosModel> findByCorreoElectronicoAgg(String correoElectronico);

    /**
     * 🧩 Buscar usuarios por rol usando aggregation
     */
    @Aggregation(pipeline = {
        "{ $match: { rolUsuario: ?0 } }",
        "{ $project: { _id: 1, nombreCompleto: 1, edad: 1, ocupacion: 1, correoElectronico: 1, telefono: 1, rolUsuario: 1 } }",
        "{ $sort: { nombreCompleto: 1 } }"
    })
    List<UsuariosModel> findByRolUsuarioAgg(String rolUsuario);
}
