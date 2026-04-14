package com.bovexo.nutritionanalysisservice.dto;
import com.bovexo.nutritionanalysisservice.model.FeedTypeEnum;

public class FeedEventDto {
  
  private Long id; 
  private String animalId;
  private FeedTypeEnum feedType;
  private Double quantity;

  public Long getId() { 
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAnimalId() {
    return animalId;
  }

  public void setAnimalId(String animalId) {
    this.animalId = animalId;
  }

  public FeedTypeEnum getFeedType() {
    return feedType;
  }

  public void setFeedType(FeedTypeEnum feedType) {
    this.feedType = feedType;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }
}