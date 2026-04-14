package com.bovexo.nutritionanalysisservice.dto;

public record TokenResponseDto(
    String accessToken,
    String tokenType,
    long expiresIn
    
) {
  public String getAccessToken() {
    return accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public long getExpiresIn() {
    return expiresIn;
  }
}