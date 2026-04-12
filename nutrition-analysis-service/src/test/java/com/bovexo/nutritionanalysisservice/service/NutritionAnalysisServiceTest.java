package com.bovexo.nutritionanalysisservice.service;

import com.bovexo.nutritionanalysisservice.dto.FeedCostDto;
import com.bovexo.nutritionanalysisservice.dto.FeedEventDto;
import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NutritionAnalysisServiceTest {

  @Mock
  private NutritionAnalysisRepository repository;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private NutritionAnalysisService service;

  @Test
  void shouldCalculateAndSaveTotalCostCorrectly() {
    FeedEventDto eventDto = new FeedEventDto();
    ReflectionTestUtils.setField(eventDto, "animalId", "123");
    ReflectionTestUtils.setField(eventDto, "feedType", com.bovexo.nutritionanalysisservice.dto.FeedType.MILHO);
    ReflectionTestUtils.setField(eventDto, "quantity", 10.0);
    
    ReflectionTestUtils.setField(service, "feedCostServiceUrl", "http://localhost:8081");
    
    FeedCostDto costDto = new FeedCostDto();
    ReflectionTestUtils.setField(costDto, "costPerKg", 250L);

    when(restTemplate.getForObject("http://localhost:8081/cost/MILHO", FeedCostDto.class))
        .thenReturn(costDto);

    service.processFeedEvent(eventDto);

    ArgumentCaptor<NutritionAnalysis> captor = ArgumentCaptor.forClass(NutritionAnalysis.class);
    verify(repository, times(1)).save(captor.capture());

    NutritionAnalysis savedAnalysis = captor.getValue();

    assertEquals(2500L, savedAnalysis.getTotalCost().longValue());
    assertEquals("123", savedAnalysis.getAnimalId());
    assertEquals(com.bovexo.nutritionanalysisservice.dto.FeedType.MILHO, savedAnalysis.getFeedType());
  }
}