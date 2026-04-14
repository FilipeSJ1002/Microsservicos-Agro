package com.bovexo.nutritionanalysisservice.dto;
import com.bovexo.nutritionanalysisservice.model.FeedTypeEnum;

public class FeedCostDto {
  private FeedTypeEnum feedType;
  private Double costPerKg;

  public Double getCostPerKg() {
    return costPerKg;
  }

  public void setCostPerKg(Double costPerKg) {
    this.costPerKg = costPerKg;
  }

  public FeedTypeEnum getFeedType() {
    return feedType;
  }

  public void setFeedType(FeedTypeEnum feedType) {
    this.feedType = feedType;
  }
}