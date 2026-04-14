package com.bovexo.feedcostservice.repository;

import com.bovexo.feedcostservice.model.FeedCost;
import com.bovexo.feedcostservice.model.FeedTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeedCostRepository extends JpaRepository<FeedCost, Long> {
  Optional<FeedCost> findByFeedType(FeedTypeEnum feedType);
}