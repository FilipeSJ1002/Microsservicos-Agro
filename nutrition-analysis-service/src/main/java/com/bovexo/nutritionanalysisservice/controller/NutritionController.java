package com.bovexo.nutritionanalysisservice.controller;

import com.bovexo.nutritionanalysisservice.dto.NutritionAnalysisDto;
import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import com.bovexo.nutritionanalysisservice.service.NutritionAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/analysis")
public class NutritionController {

  private final NutritionAnalysisRepository repository;
  private final NutritionAnalysisService nutritionAnalysisService;

  public NutritionController(NutritionAnalysisRepository repository, NutritionAnalysisService nutritionAnalysisService) {
    this.repository = repository;
    this.nutritionAnalysisService = nutritionAnalysisService;
  }

  @GetMapping
  public ResponseEntity<List<NutritionAnalysis>> getAll() {
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/{animalId}")
  public ResponseEntity<List<NutritionAnalysisDto>> getByAnimal(@PathVariable String animalId) {
    List<NutritionAnalysisDto> nutritionAnalysesDto = nutritionAnalysisService.validateAnimalId(animalId);
    return ResponseEntity.ok(nutritionAnalysesDto);
  }
}