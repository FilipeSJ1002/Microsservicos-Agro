package com.bovexo.nutritionanalysisservice.service;

import com.bovexo.nutritionanalysisservice.dto.FeedCostDto;
import com.bovexo.nutritionanalysisservice.dto.FeedEventDto;
import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.model.ProcessedEvent;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import com.bovexo.nutritionanalysisservice.repository.ProcessedEventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NutritionAnalysisService {

  private final NutritionAnalysisRepository repository;
  private final ProcessedEventRepository processedEventRepository;
  private final RestTemplate restTemplate;

  @Value("${feed.cost.service.url}")
  private String feedCostServiceUrl;

  public NutritionAnalysisService(NutritionAnalysisRepository repository, 
                                  ProcessedEventRepository processedEventRepository, 
                                  RestTemplate restTemplate) {
    this.repository = repository;
    this.processedEventRepository = processedEventRepository;
    this.restTemplate = restTemplate;
  }

  @RabbitListener(queues = "nutrition.analysis.queue")
  public void processFeedEvent(FeedEventDto event) {
    Long eventId = (long) event.getId();

    if (processedEventRepository.existsById(eventId)) {
      System.out.println("[IDEMPOTÊNCIA] Mensagem ignorada. O evento ID " + eventId + " já foi processado anteriormente.");
      return; 
    }

    String costApiUrl = feedCostServiceUrl + "/cost/" + event.getFeedType().name();
    FeedCostDto costDto = null;

    try {
      costDto = restTemplate.getForObject(costApiUrl, FeedCostDto.class);
    } catch (HttpClientErrorException.NotFound e) {
      System.err.println("[ERRO] Insumo não cadastrado no serviço de custos: " + event.getFeedType());
      return;
    } catch (RestClientException e) {
      System.err.println("[ALERTA] Serviço de custos indisponível. A mensagem voltará para a fila.");
      throw e;
    }
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
  }
}