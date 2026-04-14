package com.bovexo.auth.dto;

public record LoginResponse(
    String accessToken,
    String tokenType,
    Long expiresIn) {
}