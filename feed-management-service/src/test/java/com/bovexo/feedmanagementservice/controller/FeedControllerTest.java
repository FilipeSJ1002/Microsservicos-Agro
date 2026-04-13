package com.bovexo.feedmanagementservice.controller;

import com.bovexo.feedmanagementservice.config.RabbitMQConfig;
import com.bovexo.feedmanagementservice.model.FeedRecord;
import com.bovexo.feedmanagementservice.repository.FeedRecordRepository;
import com.bovexo.feedmanagementservice.dto.FeedRecordDto;
import com.bovexo.feedmanagementservice.model.FeedType;
import com.bovexo.feedmanagementservice.dto.FeedEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedControllerTest {

  @Mock
  private FeedRecordRepository repository;

  @Mock
  private RabbitTemplate rabbitTemplate;

  @InjectMocks
  private FeedController controller;

  @Test
  void shouldCreateFeedAndSendEventToRabbitMQ() {
    FeedRecordDto inputDto = new FeedRecordDto();
    ReflectionTestUtils.setField(inputDto, "animalId", "VACA-001");
    ReflectionTestUtils.setField(inputDto, "feedType", FeedType.MILHO);
    ReflectionTestUtils.setField(inputDto, "quantity", 10.5);

    FeedRecord savedRecord = new FeedRecord();
    ReflectionTestUtils.setField(savedRecord, "id", 1L);
    ReflectionTestUtils.setField(savedRecord, "animalId", "VACA-001");
    ReflectionTestUtils.setField(savedRecord, "feedType", FeedType.MILHO);
    ReflectionTestUtils.setField(savedRecord, "quantity", 10.5);

    when(repository.save(any(FeedRecord.class))).thenReturn(savedRecord);

    ResponseEntity<FeedRecordDto> response = controller.createFeed(inputDto);

    assertEquals(201, response.getStatusCode().value());
    assertEquals(1L, response.getBody().getId());
    assertEquals("VACA-001", response.getBody().getAnimalId());

    verify(rabbitTemplate, times(1)).convertAndSend(
        eq(RabbitMQConfig.EXCHANGE_NAME), 
        eq(RabbitMQConfig.ROUTING_KEY), 
        any(FeedEventDto.class));
  }
}