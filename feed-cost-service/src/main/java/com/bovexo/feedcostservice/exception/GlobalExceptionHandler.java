package com.bovexo.feedcostservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String errorMsg = String.format("O valor '%s' não é um tipo de insumo válido.", ex.getValue());
    return ResponseEntity.badRequest().body(Map.of(
      "erro", "Parâmetro inválido",
      "detalhe", errorMsg
    ));
  }
} 