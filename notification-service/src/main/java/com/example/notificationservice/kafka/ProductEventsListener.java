package com.example.notificationservice.kafka;

import com.example.notificationservice.model.ProductEventPayload;
import com.example.notificationservice.service.ProductCacheInvalidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsListener {

    private static final Logger log = LoggerFactory.getLogger(ProductEventsListener.class);

    private final ProductCacheInvalidationService cacheInvalidationService;

    public ProductEventsListener(ProductCacheInvalidationService cacheInvalidationService) {
        this.cacheInvalidationService = cacheInvalidationService;
    }

    @KafkaListener(topics = "product.events", groupId = "notification-service")
    public void handleProductEvent(@Payload ProductEventPayload payload) {
        log.info("New product created event received: {}", payload.getProductId());
        cacheInvalidationService.clearProductCache();
    }
}
