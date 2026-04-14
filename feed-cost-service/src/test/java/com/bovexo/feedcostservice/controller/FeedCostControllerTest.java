package com.bovexo.feedcostservice.controller;

import com.bovexo.feedcostservice.model.FeedCost;
import com.bovexo.feedcostservice.repository.FeedCostRepository;
import com.bovexo.feedcostservice.model.FeedTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedCostController.class)
class FeedCostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FeedCostRepository repository;

  @Test
  void shouldReturnFeedCostWhenExists() throws Exception {
    FeedCost mockCost = new FeedCost();
    ReflectionTestUtils.setField(mockCost, "feedType", FeedTypeEnum.MILHO);
    ReflectionTestUtils.setField(mockCost, "costPerKg", 250L);

    when(repository.findByFeedType(FeedTypeEnum.MILHO)).thenReturn(Optional.of(mockCost));

    mockMvc.perform(get("/cost/MILHO"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.feedType").value("MILHO"))
        .andExpect(jsonPath("$.costPerKg").value(250));
  }

  @Test
  void shouldReturn404WhenFeedDoesNotExist() throws Exception {
    when(repository.findByFeedType(FeedTypeEnum.MACA)).thenReturn(Optional.empty());

    mockMvc.perform(get("/cost/MACA"))
        .andExpect(status().isNotFound());
  }
}