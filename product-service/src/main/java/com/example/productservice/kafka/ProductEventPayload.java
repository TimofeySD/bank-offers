package com.example.productservice.kafka;

import java.time.Instant;

public class ProductEventPayload {

    private String eventType;
    private Long productId;
    private Long bankId;
    private Instant timestamp;

    public ProductEventPayload() {
    }

    public ProductEventPayload(String eventType, Long productId, Long bankId, Instant timestamp) {
        this.eventType = eventType;
        this.productId = productId;
        this.bankId = bankId;
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
