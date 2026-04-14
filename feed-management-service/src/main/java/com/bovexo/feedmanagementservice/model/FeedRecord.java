package com.bovexo.feedmanagementservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feed_record")
public class FeedRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "animal_id")
  private String animalId;

  @Enumerated(EnumType.STRING)
  @Column(name = "feed_type")
  private FeedTypeEnum feedType;

  private Double quantity;

  @Column(name = "created_at")
  private LocalDateTime date = LocalDateTime.now();

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

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }
}