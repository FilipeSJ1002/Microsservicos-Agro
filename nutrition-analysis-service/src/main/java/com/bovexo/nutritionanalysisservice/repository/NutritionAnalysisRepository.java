package com.bovexo.nutritionanalysisservice.repository;

import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NutritionAnalysisRepository extends MongoRepository<NutritionAnalysis, String> {
  List<NutritionAnalysis> findByAnimalId(String animalId);
}