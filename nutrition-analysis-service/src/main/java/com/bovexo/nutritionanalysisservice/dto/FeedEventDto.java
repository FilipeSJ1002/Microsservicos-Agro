package com.bovexo.nutritionanalysisservice.dto;

public class FeedEventDto {
  private String animalId;
  private String feedType;
  private Double quantity;

  public String getAnimalId() {
    return animalId;
  }

  public String getFeedType() {
    return feedType;
  }

  public Double getQuantity() {
    return quantity;
  }
}