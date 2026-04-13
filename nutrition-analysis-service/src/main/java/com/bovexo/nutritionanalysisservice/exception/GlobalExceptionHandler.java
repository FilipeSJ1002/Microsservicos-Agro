package com.bovexo.nutritionanalysisservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(404).body(Map.of(
      "erro", "Não encontrado",
      "detalhe", ex.getMessage()
    ));
  }
}