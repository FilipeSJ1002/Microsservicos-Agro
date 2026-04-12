package com.bovexo.feedcostservice.controller;

import com.bovexo.feedcostservice.model.FeedCost;
import com.bovexo.feedcostservice.model.FeedType;
import com.bovexo.feedcostservice.repository.FeedCostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cost")
public class FeedCostController {

  private final FeedCostRepository repository;

  public FeedCostController(FeedCostRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/{feedType}")
  public ResponseEntity<FeedCost> getCost(@PathVariable FeedType feedType) {
    return repository.findByFeedType(feedType)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}