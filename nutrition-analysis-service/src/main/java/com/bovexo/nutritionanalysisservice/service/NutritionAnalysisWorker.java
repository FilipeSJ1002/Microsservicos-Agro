package com.bovexo.nutritionanalysisservice.service;

import com.bovexo.nutritionanalysisservice.dto.FeedCostDto;
import com.bovexo.nutritionanalysisservice.dto.FeedEventDto;
import com.bovexo.nutritionanalysisservice.dto.TokenResponseDto;
import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.model.ProcessedEvent;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import com.bovexo.nutritionanalysisservice.repository.ProcessedEventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bovexo.nutritionanalysisservice.externalApi.FeedCostApiService;

import java.util.Map;

@Service
public class NutritionAnalysisWorker {

  private final NutritionAnalysisRepository repository;
  private final ProcessedEventRepository processedEventRepository;
  private final FeedCostApiService feedCostApiService;
  private final RestTemplate restTemplate;

  @Value("${nutrition.analysis.username}")
  private String username;

  @Value("${nutrition.analysis.password}")
  private String password;

  @Value("${auth.service.url:http://localhost:8084/auth/login}")
  private String authServiceUrl;

  public NutritionAnalysisWorker(NutritionAnalysisRepository repository,
      ProcessedEventRepository processedEventRepository,
      FeedCostApiService feedCostApiService) {
    this.repository = repository;
    this.processedEventRepository = processedEventRepository;
    this.feedCostApiService = feedCostApiService;
    this.restTemplate = new RestTemplate();
  }

  @RabbitListener(queues = "nutrition.analysis.queue")
  public void processFeedEvent(FeedEventDto event) {
    Long eventId = (long) event.getId();

    if (processedEventRepository.existsById(eventId)) {
      System.out
          .println("[IDEMPOTÊNCIA] Mensagem ignorada. O evento ID " + eventId + " já foi processado anteriormente.");
      return;
    }

    String token = getToken();

    if (token == null) {
      System.err.println("Processamento abortado: Não foi possível obter o token de autenticação.");
      return;
    }

    FeedCostDto costDto = feedCostApiService.getCost(event.getFeedType().name(), token);

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

  private String getToken() {
    try {
      Map<String, String> credentials = Map.of(
          "username", username,
          "password", password);

      ResponseEntity<TokenResponseDto> response = restTemplate.postForEntity(
          authServiceUrl,
          credentials,
          TokenResponseDto.class);

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        return response.getBody().accessToken();
      }
    } catch (Exception e) {
      System.err.println("[ERRO DE AUTENTICAÇÃO] Falha ao comunicar com Auth Service: " + e.getMessage());
    }

    return null;
  }
}