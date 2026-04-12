package com.bovexo.feedcostservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Feed Cost API", version = "1.0", description = "API para consulta do catálogo de preços de rações"))
public class FeedCostServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(FeedCostServiceApplication.class, args);
	}
}