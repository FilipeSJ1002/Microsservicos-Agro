package com.bovexo.feedmanagementservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Feed Management API", version = "1.0", description = "API para registro de consumo de ração"))
public class FeedManagementServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(FeedManagementServiceApplication.class, args);
	}
}