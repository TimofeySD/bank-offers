package com.example.productservice.service;

import com.example.productservice.config.RedisCacheConfig;
import com.example.productservice.dto.ProductFilter;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.kafka.ProductEventPayload;
import com.example.productservice.kafka.config.ProductEventTopics;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.specification.ProductSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CacheManager cacheManager;

    public ProductService(ProductRepository productRepository,
                          KafkaTemplate<String, Object> kafkaTemplate,
                          CacheManager cacheManager) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = RedisCacheConfig.PRODUCTS_CACHE,
        key = "#filter.cacheKey(#pageable.pageNumber, #pageable.pageSize, #pageable.sort.toString())")
    @Transactional(readOnly = true)
    public Page<ProductResponse> findProducts(ProductFilter filter, Pageable pageable) {
        return productRepository.findAll(ProductSpecifications.build(filter.getType().orElse(null),
                filter.getMaxRate().orElse(null),
                filter.getBankId().orElse(null)), pageable)
            .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setBankId(request.getBankId());
        product.setType(request.getType());
        product.setName(request.getName());
        product.setRate(request.getRate());
        product.setTermMonths(request.getTermMonths());
        product.setCurrency(request.getCurrency());

        Product saved = productRepository.save(product);
        publishProductCreatedEvent(saved);
        evictProductCache();
        return toResponse(saved);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getBankId(), product.getType(), product.getName(),
            product.getRate(), product.getTermMonths(), product.getCurrency());
    }

    private void publishProductCreatedEvent(Product product) {
        ProductEventPayload payload = new ProductEventPayload("NEW_PRODUCT_CREATED", product.getId(),
            product.getBankId(), Instant.now());
        kafkaTemplate.send(ProductEventTopics.PRODUCT_EVENTS_TOPIC, String.valueOf(product.getId()), payload);
        log.info("Sent product event: {}", payload.getEventType());
    }

    private void evictProductCache() {
        Cache cache = cacheManager.getCache(RedisCacheConfig.PRODUCTS_CACHE);
        if (cache != null) {
            cache.clear();
        }
    }
}
