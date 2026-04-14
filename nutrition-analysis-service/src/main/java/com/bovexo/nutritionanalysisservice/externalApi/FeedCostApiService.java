package com.bovexo.nutritionanalysisservice.externalApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bovexo.nutritionanalysisservice.dto.FeedCostDto;

@Service
public class FeedCostApiService {

  private final RestTemplate restTemplate;

  @Value("${feed.cost.service.url}")
  private String feedCostServiceUrl;

  public FeedCostApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public FeedCostDto getCost(String feedType) {
    String costApiUrl = feedCostServiceUrl + "/cost/" + feedType;
    FeedCostDto costDto = null;

    try {
      costDto = restTemplate.getForObject(costApiUrl, FeedCostDto.class);
    } catch (HttpClientErrorException.NotFound e) {
      System.err.println("[ERRO] Insumo não cadastrado no serviço de custos: " + feedType);
      throw e;
    } catch (RestClientException e) {
      System.err.println("[ALERTA] Serviço de custos indisponível. A mensagem voltará para a fila.");
      throw e;
    }

    return costDto;
  }
}
