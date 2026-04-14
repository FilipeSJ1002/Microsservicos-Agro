package com.bovexo.feedcostservice.dto;

import com.bovexo.feedcostservice.model.FeedTypeEnum;

public record FeedCostResponse(
    FeedTypeEnum feedType,
    Double costPerKg
) {}