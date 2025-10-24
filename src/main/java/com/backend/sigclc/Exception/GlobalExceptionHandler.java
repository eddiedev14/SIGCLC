package com.backend.sigclc.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        String mensajeError = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");

        Map<String, String> response = new HashMap<>();
        response.put("error", mensajeError);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
    Map<String, String> response = new HashMap<>();

    // Puedes personalizar el mensaje según lo que detectes
    String mensaje = ex.getMessage();

    if (mensaje.contains("telefono") && mensaje.contains("minLength")) {
        mensaje = "El teléfono debe tener exactamente 10 dígitos numéricos";
    } else if (mensaje.contains("correo") && mensaje.contains("pattern")) {
        mensaje = "Falta el @ en el correo electrónico";
    } else {
        mensaje = "Error de validación en los datos enviados";
    }

    response.put("error", mensaje);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}

}
