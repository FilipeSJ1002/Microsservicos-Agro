package com.bovexo.feedcostservice.controller;

import com.bovexo.feedcostservice.dto.FeedCostResponse;
import com.bovexo.feedcostservice.model.FeedTypeEnum;
import com.bovexo.feedcostservice.service.FeedCostService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cost")
public class FeedCostController {

  private final FeedCostService feedCostService;

  public FeedCostController(FeedCostService feedCostService) {
    this.feedCostService = feedCostService;
  }

  @GetMapping("/{feedType}")
  public ResponseEntity<FeedCostResponse> getCost(@PathVariable FeedTypeEnum feedType) {
    FeedCostResponse response = feedCostService.getCostByFeedType(feedType);
    return ResponseEntity.ok(response);
  }
}