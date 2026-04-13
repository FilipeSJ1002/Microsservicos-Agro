package com.bovexo.nutritionanalysisservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "processed_events")
public class ProcessedEvent {

    @Id
    private Long eventId;
    private LocalDateTime processedAt;

    public ProcessedEvent(Long eventId) {
        this.eventId = eventId;
        this.processedAt = LocalDateTime.now();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
