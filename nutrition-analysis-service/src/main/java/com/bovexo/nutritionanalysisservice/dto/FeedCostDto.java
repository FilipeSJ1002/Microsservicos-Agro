package com.bovexo.nutritionanalysisservice.dto;

public class FeedCostDto {
  private com.bovexo.nutritionanalysisservice.dto.FeedType feedType;
  private Long costPerKg;

  public Long getCostPerKg() {
    return costPerKg;
  }

  public void setCostPerKg(Long costPerKg) {
    this.costPerKg = costPerKg;
  }

  public com.bovexo.nutritionanalysisservice.dto.FeedType getFeedType() {
    return feedType;
  }

  public void setFeedType(com.bovexo.nutritionanalysisservice.dto.FeedType feedType) {
    this.feedType = feedType;
  }
}