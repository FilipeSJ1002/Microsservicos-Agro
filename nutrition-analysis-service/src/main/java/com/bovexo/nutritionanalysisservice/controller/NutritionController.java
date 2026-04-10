package com.bovexo.nutritionanalysisservice.controller;

import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/analysis")
public class NutritionController {

  private final NutritionAnalysisRepository repository;

  public NutritionController(NutritionAnalysisRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<List<NutritionAnalysis>> getAll() {
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/{animalId}")
  public ResponseEntity<List<NutritionAnalysis>> getByAnimal(@PathVariable String animalId) {
    return ResponseEntity.ok(repository.findByAnimalId(animalId));
  }
}