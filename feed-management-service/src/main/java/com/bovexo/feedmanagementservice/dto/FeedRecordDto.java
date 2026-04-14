package com.bovexo.feedmanagementservice.dto;

import com.bovexo.feedmanagementservice.model.FeedTypeEnum;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class FeedRecordDto {

  private Long id;

  @NotBlank(message = "animalId é obrigatório")
  private String animalId;

  @NotNull(message = "FeedType é obrigatório")
  private FeedTypeEnum feedType;

  @NotNull(message = "Quantidade é obrigatória")
  @Positive(message = "Quantidade deve ser maior que zero")
  @DecimalMax(value = "1000.0", message = "Quantidade não pode ser maior que 1000")
  private Double quantity;

  private LocalDateTime recordDate;

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

  public LocalDateTime getRecordDate() {
    return recordDate;
  }

  public void setRecordDate(LocalDateTime recordDate) {
    this.recordDate = recordDate;
  }
}
