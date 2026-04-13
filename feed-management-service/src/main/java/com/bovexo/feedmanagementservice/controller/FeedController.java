package com.bovexo.feedmanagementservice.controller;

import com.bovexo.feedmanagementservice.config.RabbitMQConfig;
import com.bovexo.feedmanagementservice.model.FeedRecord;
import com.bovexo.feedmanagementservice.repository.FeedRecordRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bovexo.feedmanagementservice.dto.FeedRecordDto;
import com.bovexo.feedmanagementservice.exception.ResourceNotFoundException;
import com.bovexo.feedmanagementservice.dto.FeedEventDto;
import org.springframework.http.HttpStatus;
import java.util.stream.Collectors;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feeds")
public class FeedController {

  private final FeedRecordRepository repository;
  private final RabbitTemplate rabbitTemplate;

  public FeedController(FeedRecordRepository repository, RabbitTemplate rabbitTemplate) {
    this.repository = repository;
    this.rabbitTemplate = rabbitTemplate;
  }

  @PostMapping
  public ResponseEntity<FeedRecordDto> createFeed(@RequestBody FeedRecordDto dto) {
    FeedRecord record = new FeedRecord();
    record.setAnimalId(dto.getAnimalId());
    record.setFeedType(dto.getFeedType());
    record.setQuantity(dto.getQuantity());
    if (dto.getRecordDate() != null) {
      record.setDate(dto.getRecordDate());
    }

    FeedRecord savedRecord = repository.save(record);

    FeedEventDto eventDto = new FeedEventDto();
    eventDto.setId(savedRecord.getId());
    eventDto.setAnimalId(savedRecord.getAnimalId());
    eventDto.setFeedType(savedRecord.getFeedType());
    eventDto.setQuantity(savedRecord.getQuantity());

    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, eventDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedRecord));
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
      throw new ResourceNotFoundException("Nenhum registro de alimentação encontrado para o animal ID: " + animalId);
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