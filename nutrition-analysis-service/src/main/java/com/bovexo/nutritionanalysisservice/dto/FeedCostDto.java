package com.bovexo.nutritionanalysisservice.dto;

import java.math.BigDecimal;

public class FeedCostDto {
  private String feedType;
  private BigDecimal costPerKg;

  public BigDecimal getCostPerKg() {
    return costPerKg;
  }

  public String getFeedType() {
    return feedType;
  }
}