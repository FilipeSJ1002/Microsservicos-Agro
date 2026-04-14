package com.bovexo.feedcostservice.service;

import com.bovexo.feedcostservice.dto.FeedCostResponse;
import com.bovexo.feedcostservice.model.FeedTypeEnum;

public interface IFeedCostService {
  FeedCostResponse getCostByFeedType(FeedTypeEnum feedType);
}
