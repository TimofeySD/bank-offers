package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ProductCacheInvalidationService {

    private static final Logger log = LoggerFactory.getLogger(ProductCacheInvalidationService.class);
    private static final String PRODUCT_CACHE_PATTERN = "product-service::products*";

    private final StringRedisTemplate redisTemplate;

    public ProductCacheInvalidationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void clearProductCache() {
        redisTemplate.execute(connection -> {
            try (var cursor = connection.keyCommands().scan(ScanOptions.scanOptions()
                .match(PRODUCT_CACHE_PATTERN)
                .count(100)
                .build())) {
                cursor.forEachRemaining(key -> {
                    connection.keyCommands().del(key);
                    log.debug("Deleted cache key: {}", new String(key, StandardCharsets.UTF_8));
                });
            } catch (Exception ex) {
                log.warn("Failed to iterate redis keys", ex);
            }
            return null;
        }, true, true);
        log.info("Product cache invalidated");
    }
}
