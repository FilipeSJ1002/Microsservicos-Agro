package com.bovexo.feedmanagementservice.controller;

import com.bovexo.feedmanagementservice.model.FeedRecord;
import com.bovexo.feedmanagementservice.repository.FeedRecordRepository;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bovexo.feedmanagementservice.dto.FeedRecordDto;
import com.bovexo.feedmanagementservice.service.IFeedManagementService;
import org.springframework.http.HttpStatus;
import java.util.stream.Collectors;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feeds")
public class FeedController {

  private final FeedRecordRepository repository;
  private final IFeedManagementService feedPostService;

  public FeedController(FeedRecordRepository repository, IFeedManagementService feedPostService) {
    this.repository = repository;
    this.feedPostService = feedPostService;
  }

  @PostMapping
  public ResponseEntity<FeedRecordDto> createFeed(@RequestBody @Valid FeedRecordDto dto) {
    FeedRecordDto responseDto = feedPostService.createFeed(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping
  public ResponseEntity<List<FeedRecordDto>> getAllFeeds() {
    List<FeedRecordDto> dtos = repository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{animalId}")
  public ResponseEntity<List<FeedRecordDto>> getFeedsByAnimal(@PathVariable String animalId) {
    List<FeedRecord> records = repository.findByAnimalId(animalId);
    
    if (records.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    List<FeedRecordDto> dtos = records.stream()
      .map(this::toDto)
      .collect(Collectors.toList());
        
    return ResponseEntity.ok(dtos);
  }

  private FeedRecordDto toDto(FeedRecord record) {
    FeedRecordDto dto = new FeedRecordDto();
    dto.setId(record.getId());
    dto.setAnimalId(record.getAnimalId());
    dto.setFeedType(record.getFeedType());
    dto.setQuantity(record.getQuantity());
    dto.setRecordDate(record.getDate());
    return dto;
  }
}