package com.bovexo.feedmanagementservice.controller;

import com.bovexo.feedmanagementservice.dto.FeedRecordDto;
import com.bovexo.feedmanagementservice.service.IFeedManagementService;
import com.bovexo.feedmanagementservice.service.IFeedQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

  private final IFeedManagementService feedCommandService;
  private final IFeedQueryService feedQueryService;

  @PostMapping
  public ResponseEntity<FeedRecordDto> createFeed(@RequestBody @Valid FeedRecordDto dto) {
    FeedRecordDto responseDto = feedCommandService.createFeed(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping
  public ResponseEntity<List<FeedRecordDto>> getAllFeeds() {
    return ResponseEntity.ok(feedQueryService.getAllFeeds());
  }

  @GetMapping("/{animalId}")
  public ResponseEntity<List<FeedRecordDto>> getFeedsByAnimal(@PathVariable String animalId) {
    return ResponseEntity.ok(feedQueryService.getFeedsByAnimalId(animalId));
  }
}