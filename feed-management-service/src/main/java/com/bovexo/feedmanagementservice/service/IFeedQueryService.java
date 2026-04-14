package com.bovexo.feedmanagementservice.service;

import com.bovexo.feedmanagementservice.dto.FeedRecordDto;
import java.util.List;

public interface IFeedQueryService {
  List<FeedRecordDto> getAllFeeds();
  List<FeedRecordDto> getFeedsByAnimalId(String animalId);
}