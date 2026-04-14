package com.bovexo.feedmanagementservice.service;

import com.bovexo.feedmanagementservice.dto.FeedRecordDto;

public interface IFeedManagementService {
  FeedRecordDto createFeed(FeedRecordDto dto);
}
