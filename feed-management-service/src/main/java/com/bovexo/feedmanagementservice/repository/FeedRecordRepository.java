package com.bovexo.feedmanagementservice.repository;

import com.bovexo.feedmanagementservice.model.FeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedRecordRepository extends JpaRepository<FeedRecord, Long> {
  List<FeedRecord> findByAnimalId(String animalId);
}