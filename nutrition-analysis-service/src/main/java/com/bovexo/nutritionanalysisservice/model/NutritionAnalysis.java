package com.bovexo.nutritionanalysisservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "nutrition_analysis")
public class NutritionAnalysis {
  @Id
  private String id;
  private String animalId;
  private com.bovexo.nutritionanalysisservice.dto.FeedType feedType;
  private Double quantity;
  private Long costPerKg;
  private Long totalCost;
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

  public com.bovexo.nutritionanalysisservice.dto.FeedType getFeedType() {
    return feedType;
  }

  public void setFeedType(com.bovexo.nutritionanalysisservice.dto.FeedType feedType) {
    this.feedType = feedType;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public Long getCostPerKg() {
    return costPerKg;
  }

  public void setCostPerKg(Long costPerKg) {
    this.costPerKg = costPerKg;
  }

  public Long getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(Long totalCost) {
    this.totalCost = totalCost;
  }

  public LocalDateTime getAnalysisDate() {
    return analysisDate;
  }

  public void setAnalysisDate(LocalDateTime analysisDate) {
    this.analysisDate = analysisDate;
  }
}