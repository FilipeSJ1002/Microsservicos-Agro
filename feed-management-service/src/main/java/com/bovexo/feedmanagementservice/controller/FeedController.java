package com.bovexo.feedmanagementservice.controller;

import com.bovexo.feedmanagementservice.config.RabbitMQConfig;
import com.bovexo.feedmanagementservice.model.FeedRecord;
import com.bovexo.feedmanagementservice.repository.FeedRecordRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public ResponseEntity<FeedRecord> createFeed(@RequestBody FeedRecord record) {
    FeedRecord savedRecord = repository.save(record);

    rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, savedRecord);

    return ResponseEntity.ok(savedRecord);
  }

  @GetMapping
  public ResponseEntity<List<FeedRecord>> getAllFeeds() {
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/{animalId}")
  public ResponseEntity<List<FeedRecord>> getFeedsByAnimal(@PathVariable String animalId) {
    return ResponseEntity.ok(repository.findByAnimalId(animalId));
  }
}