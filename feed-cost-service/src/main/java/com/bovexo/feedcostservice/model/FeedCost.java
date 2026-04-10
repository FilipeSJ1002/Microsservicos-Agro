package com.bovexo.feedcostservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "feed_cost")
public class FeedCost {

  @Id
  @JsonIgnore
  private Long id;

  @Column(name = "feed_type")
  private String feedType;

  @Column(name = "cost_per_kg")
  private BigDecimal costPerKg;

  @Column(name = "last_update")
  @JsonIgnore
  private LocalDateTime lastUpdate;

  public String getFeedType() {
    return feedType;
  }

  public BigDecimal getCostPerKg() {
    return costPerKg;
  }
}