package com.bovexo.feedmanagementservice.dto;

import com.bovexo.feedmanagementservice.model.FeedType;

public class FeedEventDto {
  
  private Long id; 
  private String animalId;
  private FeedType feedType;
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

  public FeedType getFeedType() {
    return feedType;
  }

  public void setFeedType(FeedType feedType) {
    this.feedType = feedType;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }
}