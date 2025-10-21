package com.backend.sigclc.Exception;

public class RecursoNoEncontradoException  extends RuntimeException{
    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);    
    }
}