package com.bovexo.nutritionanalysisservice.service;

import com.bovexo.nutritionanalysisservice.dto.FeedCostDto;
import com.bovexo.nutritionanalysisservice.dto.FeedEventDto;
import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class NutritionAnalysisService {

  private final NutritionAnalysisRepository repository;
  private final RestTemplate restTemplate;

  @Value("${feed.cost.service.url}")
  private String feedCostServiceUrl;

  public NutritionAnalysisService(NutritionAnalysisRepository repository, RestTemplate restTemplate) {
    this.repository = repository;
    this.restTemplate = restTemplate;
  }

  @RabbitListener(queues = "nutrition.analysis.queue")
  public void processFeedEvent(FeedEventDto event) {
    String costApiUrl = feedCostServiceUrl + "/cost/" + event.getFeedType().name();
    FeedCostDto costDto = restTemplate.getForObject(costApiUrl, FeedCostDto.class);

    if (costDto != null && costDto.getCostPerKg() != null) {
      Long totalCost = Math.round(costDto.getCostPerKg() * event.getQuantity());

      NutritionAnalysis analysis = new NutritionAnalysis();
      analysis.setAnimalId(event.getAnimalId());
      analysis.setFeedType(event.getFeedType());
      analysis.setQuantity(event.getQuantity());
      analysis.setCostPerKg(costDto.getCostPerKg());
      analysis.setTotalCost(totalCost);

      repository.save(analysis);
      System.out.println("Análise salva com sucesso para o animal: " + event.getAnimalId());
    }
  }
}