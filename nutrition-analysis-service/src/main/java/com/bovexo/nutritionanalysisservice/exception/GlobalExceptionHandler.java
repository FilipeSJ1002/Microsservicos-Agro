package com.bovexo.nutritionanalysisservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.Map;

@Hidden
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