package com.bovexo.feedcostservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feed_cost")
public class FeedCost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "feed_type")
  private FeedTypeEnum feedType;

  @Column(name = "cost_per_kg")
  private Long costPerKg;

  @Column(name = "last_update")
  @JsonIgnore
  private LocalDateTime lastUpdate;

  public Long getId() {
    return id;
  }

  public FeedTypeEnum getFeedType() {
    return feedType;
  }

  public void setFeedType(FeedTypeEnum feedType) {
    this.feedType = feedType;
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

  public LocalDateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(LocalDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}