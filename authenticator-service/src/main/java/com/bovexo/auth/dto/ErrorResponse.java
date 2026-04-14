package com.bovexo.auth.dto;

public record ErrorResponse(
    String code,
    String message) {
}