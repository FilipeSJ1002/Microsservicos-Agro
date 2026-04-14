package com.bovexo.feedmanagementservice.service;

import com.bovexo.feedmanagementservice.config.RabbitMQConfig;
import com.bovexo.feedmanagementservice.dto.FeedEventDto;
import com.bovexo.feedmanagementservice.dto.FeedRecordDto;
import com.bovexo.feedmanagementservice.model.FeedRecord;
import com.bovexo.feedmanagementservice.repository.FeedRecordRepository;
import org.modelmapper.ModelMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeedManagementService implements IFeedManagementService {

  private final RabbitTemplate rabbitTemplate;
  private final FeedRecordRepository repository;
  private final ModelMapper modelMapper;

  public FeedManagementService(RabbitTemplate rabbitTemplate, FeedRecordRepository repository,
      ModelMapper modelMapper) {
    this.rabbitTemplate = rabbitTemplate;
    this.repository = repository;
    this.modelMapper = modelMapper;
  }

  private FeedRecord saveFeedRecord(FeedRecordDto FeedRecordDto) {
    FeedRecord record = new FeedRecord();
    record.setAnimalId(FeedRecordDto.getAnimalId());
    record.setFeedType(FeedRecordDto.getFeedType());
    record.setQuantity(FeedRecordDto.getQuantity());
    if (FeedRecordDto.getRecordDate() != null) {
      record.setDate(FeedRecordDto.getRecordDate());
    }

    FeedRecord savedRecord = repository.save(record);

    return savedRecord;
  }

  private void postFeedEvent(FeedRecord feedRecord) {
    FeedEventDto eventDto = new FeedEventDto();
    eventDto.setId(feedRecord.getId());
    eventDto.setAnimalId(feedRecord.getAnimalId());
    eventDto.setFeedType(feedRecord.getFeedType());
    eventDto.setQuantity(feedRecord.getQuantity());

    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, eventDto);
  }

  public FeedRecordDto createFeed(FeedRecordDto dto) {
    FeedRecord savedRecord = saveFeedRecord(dto);
    postFeedEvent(savedRecord);

    FeedRecordDto responseDto = modelMapper.map(savedRecord, FeedRecordDto.class);

    return responseDto;
  }
}
