package com.bovexo.feedmanagementservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(404).body(Map.of(
        "erro", "Não encontrado",
        "detalhe", ex.getMessage()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleMessageNotReadable(HttpMessageNotReadableException ex) {
    String mensagemDetalhada = "Erro na leitura do JSON. Verifique se os dados foram enviados no formato correto.";
    String erroRaiz = ex.getMostSpecificCause().getMessage();
    if (erroRaiz != null && erroRaiz.contains("not one of the values accepted for Enum class")) {
      mensagemDetalhada = "O tipo de alimentação informado não existe. Verifique os valores permitidos (ex: MILHO, SOJA, etc) e envie sem acentos ou espaços.";
    }

    return ResponseEntity.badRequest().body(Map.of(
        "erro", "Requisição mal formatada",
        "detalhe", mensagemDetalhada));
  }
}