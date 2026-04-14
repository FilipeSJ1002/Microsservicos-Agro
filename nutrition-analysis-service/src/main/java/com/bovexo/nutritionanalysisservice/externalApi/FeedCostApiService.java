package com.bovexo.nutritionanalysisservice.externalApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.bovexo.nutritionanalysisservice.dto.FeedCostDto;

@Service
public class FeedCostApiService {

  private final RestTemplate restTemplate;

  @Value("${feed.cost.service.url}")
  private String feedCostServiceUrl;

  public FeedCostApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public FeedCostDto getCost(String feedType, String token) {
    String costApiUrl = feedCostServiceUrl + "/cost/" + feedType;
    FeedCostDto costDto = null;

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + token);

      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

      ResponseEntity<FeedCostDto> response = restTemplate.exchange(
          costApiUrl,
          HttpMethod.GET,
          requestEntity,
          FeedCostDto.class);

      costDto = response.getBody();

    } catch (NotFound e) {
      System.err.println("[ERRO] Insumo não cadastrado no serviço de custos: " + feedType);
      throw e;
    } catch (RestClientException e) {
      System.err.println("[ALERTA] Serviço de custos indisponível. A mensagem voltará para a fila.");
      throw e;
    }

    return costDto;
  }
}
