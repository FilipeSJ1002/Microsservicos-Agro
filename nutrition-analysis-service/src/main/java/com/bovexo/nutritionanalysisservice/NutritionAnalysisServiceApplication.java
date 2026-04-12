package com.bovexo.nutritionanalysisservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Nutrition Analysis API", version = "1.0", description = "API para processamento e geração de relatórios de custos nutricionais"))
public class NutritionAnalysisServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NutritionAnalysisServiceApplication.class, args);
	}
}