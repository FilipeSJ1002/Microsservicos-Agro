package com.bovexo.feedcostservice.service;

import com.bovexo.feedcostservice.dto.FeedCostResponse;
import com.bovexo.feedcostservice.model.FeedCost;
import com.bovexo.feedcostservice.model.FeedTypeEnum;
import com.bovexo.feedcostservice.repository.FeedCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FeedCostService implements IFeedCostService {

    private final FeedCostRepository repository;

    @Override
    public FeedCostResponse getCostByFeedType(FeedTypeEnum feedType) {
        FeedCost feedCost = repository.findByFeedType(feedType)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Custo não encontrado para o insumo: " + feedType));

        return new FeedCostResponse(
            feedCost.getFeedType(), 
            feedCost.getCostPerKgReais() 
        );
    }
}