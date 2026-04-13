package com.bovexo.nutritionanalysisservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "nutrition_analysis")
public class NutritionAnalysis {
  @Id
  private String id;
  private String animalId;
  private FeedType feedType;
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

  @JsonIgnore
  public Long getCostPerKg() {
    return costPerKg;
  }

  @JsonProperty("costPerKg")
  public Double getCostPerKgReais() {
    return costPerKg != null ? costPerKg / 100.0 : null;
  }

  public void setCostPerKg(Long costPerKg) {
    this.costPerKg = costPerKg;
  }

  @JsonIgnore
  public Long getTotalCost() {
    return totalCost;
  }

  @JsonProperty("totalCost")
  public Double getTotalCostReais() {
    return totalCost != null ? totalCost / 100.0 : null;
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