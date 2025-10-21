package com.backend.sigclc.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sigclc.DTO.Usuarios.UsuarioCreateDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioResponseDTO;
import com.backend.sigclc.DTO.Usuarios.UsuarioUpdateDTO;
import com.backend.sigclc.Model.Usuarios.UsuariosModel;
import com.backend.sigclc.Service.Usuarios.IUsuariosService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping ("") // endpoint
public class UsuariosController {
    @Autowired IUsuariosService usuariosService;
    @PostMapping("/insertar")
    public ResponseEntity<UsuarioResponseDTO> crearEmpleado(@RequestBody UsuarioCreateDTO usuario){
        return new ResponseEntity<UsuarioResponseDTO>(usuariosService.guardarUsuario(usuario),HttpStatus.CREATED);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarEmpleados(){
        return new ResponseEntity<List<UsuarioResponseDTO>>(usuariosService.listarUsuarios(),HttpStatus.OK);
    }

    @GetMapping("/empleadoporid/{id}")
    public ResponseEntity<UsuariosModel> buscarEmpleadosPorId(@PathVariable ObjectId id){
        return new ResponseEntity<UsuariosModel>(usuariosService.buscarUsuariosPorId(id),HttpStatus.OK);
    }

}