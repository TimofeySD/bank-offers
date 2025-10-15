package com.example.productservice.dto;

import java.math.BigDecimal;
import java.util.Optional;

public class ProductFilter {

    private final String type;
    private final BigDecimal maxRate;
    private final Long bankId;

    public ProductFilter(String type, BigDecimal maxRate, Long bankId) {
        this.type = type;
        this.maxRate = maxRate;
        this.bankId = bankId;
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<BigDecimal> getMaxRate() {
        return Optional.ofNullable(maxRate);
    }

    public Optional<Long> getBankId() {
        return Optional.ofNullable(bankId);
    }

    public String cacheKey(int page, int size, String sort) {
        return "%s:%s:%s:%s:%s:%s".formatted(page, size,
            sort == null ? "*" : sort,
            type == null ? "*" : type.toLowerCase(),
            maxRate == null ? "*" : maxRate.toPlainString(),
            bankId == null ? "*" : bankId);
    }
}
