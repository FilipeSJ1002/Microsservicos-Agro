package com.bovexo.feedmanagementservice.service;

import com.bovexo.feedmanagementservice.dto.FeedRecordDto;
import com.bovexo.feedmanagementservice.model.FeedRecord;
import com.bovexo.feedmanagementservice.repository.FeedRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedQueryService implements IFeedQueryService {

  private final FeedRecordRepository repository;
  private final ModelMapper modelMapper;

  @Override
  public List<FeedRecordDto> getAllFeeds() {
    return repository.findAll().stream()
        .map(record -> modelMapper.map(record, FeedRecordDto.class))
        .collect(Collectors.toList());
  }

  @Override
  public List<FeedRecordDto> getFeedsByAnimalId(String animalId) {
    List<FeedRecord> records = repository.findByAnimalId(animalId);

    if (records.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Nenhum registro de alimentação encontrado para o animal: " + animalId);
    }

    return records.stream()
        .map(record -> modelMapper.map(record, FeedRecordDto.class))
        .collect(Collectors.toList());
  }
}