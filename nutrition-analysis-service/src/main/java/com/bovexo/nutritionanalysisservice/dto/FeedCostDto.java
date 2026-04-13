package com.bovexo.nutritionanalysisservice.dto;
import com.bovexo.nutritionanalysisservice.model.FeedType;

public class FeedCostDto {
  private FeedType feedType;
  private Double costPerKg;

  public Double getCostPerKg() {
    return costPerKg;
  }

  public void setCostPerKg(Double costPerKg) {
    this.costPerKg = costPerKg;
  }

  public FeedType getFeedType() {
    return feedType;
  }

  public void setFeedType(FeedType feedType) {
    this.feedType = feedType;
  }
}