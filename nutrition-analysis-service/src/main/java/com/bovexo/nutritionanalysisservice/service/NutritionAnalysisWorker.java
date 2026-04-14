package com.bovexo.nutritionanalysisservice.service;

import com.bovexo.nutritionanalysisservice.dto.FeedCostDto;
import com.bovexo.nutritionanalysisservice.dto.FeedEventDto;
import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.model.ProcessedEvent;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import com.bovexo.nutritionanalysisservice.repository.ProcessedEventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.bovexo.nutritionanalysisservice.externalApi.FeedCostApiService;

@Service
public class NutritionAnalysisWorker {

  private final NutritionAnalysisRepository repository;
  private final ProcessedEventRepository processedEventRepository;
  private final FeedCostApiService feedCostApiService;

  public NutritionAnalysisWorker(NutritionAnalysisRepository repository, 
                                  ProcessedEventRepository processedEventRepository, 
                                  FeedCostApiService feedCostApiService) {
    this.repository = repository;
    this.processedEventRepository = processedEventRepository;
    this.feedCostApiService = feedCostApiService;
  }

  @RabbitListener(queues = "nutrition.analysis.queue")
  public void processFeedEvent(FeedEventDto event) {
    Long eventId = (long) event.getId();

    if (processedEventRepository.existsById(eventId)) {
      System.out.println("[IDEMPOTÊNCIA] Mensagem ignorada. O evento ID " + eventId + " já foi processado anteriormente.");
      return; 
    }
    
    FeedCostDto costDto = feedCostApiService.getCost(event.getFeedType().name());

    if (costDto != null && costDto.getCostPerKg() != null) {
      Long costPerKgCents = Math.round(costDto.getCostPerKg() * 100.0);
      Long totalCost = Math.round(costPerKgCents * event.getQuantity());

      NutritionAnalysis analysis = new NutritionAnalysis();
      analysis.setAnimalId(event.getAnimalId());
      analysis.setFeedType(event.getFeedType());
      analysis.setQuantity(event.getQuantity());
      analysis.setCostPerKg(costPerKgCents);
      analysis.setTotalCost(totalCost);

      repository.save(analysis);
      System.out.println("Análise salva com sucesso para o animal: " + event.getAnimalId());

      processedEventRepository.save(new ProcessedEvent(eventId));
    }

    return;
  }
}