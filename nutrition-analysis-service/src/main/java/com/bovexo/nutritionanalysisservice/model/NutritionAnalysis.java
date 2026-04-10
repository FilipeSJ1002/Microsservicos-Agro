package com.bovexo.nutritionanalysisservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "nutrition_analysis")
public class NutritionAnalysis {
  @Id
  private String id;
  private String animalId;
  private String feedType;
  private Double quantity;
  private BigDecimal costPerKg;
  private BigDecimal totalCost;
  private LocalDateTime analysisDate = LocalDateTime.now();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAnimalId() {
    return animalId;
  }

  public void setAnimalId(String animalId) {
    this.animalId = animalId;
  }

  public String getFeedType() {
    return feedType;
  }

  public void setFeedType(String feedType) {
    this.feedType = feedType;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getCostPerKg() {
    return costPerKg;
  }

  public void setCostPerKg(BigDecimal costPerKg) {
    this.costPerKg = costPerKg;
  }

  public BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;
  }

  public LocalDateTime getAnalysisDate() {
    return analysisDate;
  }

  public void setAnalysisDate(LocalDateTime analysisDate) {
    this.analysisDate = analysisDate;
  }
}