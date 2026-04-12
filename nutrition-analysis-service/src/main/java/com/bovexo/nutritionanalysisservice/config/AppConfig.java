package com.bovexo.nutritionanalysisservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  public static final String QUEUE_NAME = "nutrition.analysis.queue";
  public static final String EXCHANGE_NAME = "nutrition.exchange";
  public static final String ROUTING_KEY = "nutrition.routing.key";

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public JacksonJsonMessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public Queue nutritionAnalysisQueue() {
    return new Queue(QUEUE_NAME, true);
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public Binding binding(Queue nutritionAnalysisQueue, TopicExchange exchange) {
    return BindingBuilder.bind(nutritionAnalysisQueue).to(exchange).with(ROUTING_KEY);
  }
}