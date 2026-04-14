package com.bovexo.nutritionanalysisservice.service;

import java.util.List;

import com.bovexo.nutritionanalysisservice.dto.NutritionAnalysisDto;
import com.bovexo.nutritionanalysisservice.exception.ResourceNotFoundException;
import com.bovexo.nutritionanalysisservice.model.NutritionAnalysis;
import com.bovexo.nutritionanalysisservice.repository.NutritionAnalysisRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class NutritionAnalysisService {

  private final NutritionAnalysisRepository repository;
  private final ModelMapper modelMapper;

  public NutritionAnalysisService(NutritionAnalysisRepository repository, ModelMapper modelMapper) {
    this.repository = repository;
    this.modelMapper = modelMapper;
  }

  public List<NutritionAnalysisDto> validateAnimalId(String animalId) {
    List<NutritionAnalysis> analyses = repository.findByAnimalId(animalId);
    if (analyses.isEmpty()) {
      throw new ResourceNotFoundException("Nenhuma análise encontrada para o animal ID: " + animalId);
    }
    List<NutritionAnalysisDto> nutritionAnalysesDto = analyses.stream().map(analysis -> modelMapper.map(analysis, NutritionAnalysisDto.class)).toList();
    return nutritionAnalysesDto;
  }
}
