package com.bovexo.nutritionanalysisservice.repository;

import com.bovexo.nutritionanalysisservice.model.ProcessedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessedEventRepository extends MongoRepository<ProcessedEvent, Long> {
}