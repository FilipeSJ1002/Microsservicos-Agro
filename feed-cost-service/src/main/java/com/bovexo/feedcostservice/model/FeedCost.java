package com.bovexo.feedcostservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feed_cost")
public class FeedCost {

  @Id
  @JsonIgnore
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "feed_type")
  private FeedType feedType;

  @Column(name = "cost_per_kg")
  private Long costPerKg;

  @Column(name = "last_update")
  @JsonIgnore
  private LocalDateTime lastUpdate;

  public Long getId() {
    return id;
  }

  public FeedType getFeedType() {
    return feedType;
  }

  public void setFeedType(FeedType feedType) {
    this.feedType = feedType;
  }

  public Long getCostPerKg() {
    return costPerKg;
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