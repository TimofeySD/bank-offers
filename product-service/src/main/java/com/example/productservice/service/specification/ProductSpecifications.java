package com.example.productservice.service.specification;

import com.example.productservice.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> typeEquals(String type) {
        return (root, query, cb) -> type == null ? null : cb.equal(cb.lower(root.get("type")), type.toLowerCase());
    }

    public static Specification<Product> bankEquals(Long bankId) {
        return (root, query, cb) -> bankId == null ? null : cb.equal(root.get("bankId"), bankId);
    }

    public static Specification<Product> rateLessThan(BigDecimal maxRate) {
        return (root, query, cb) -> maxRate == null ? null : cb.lessThanOrEqualTo(root.get("rate"), maxRate);
    }

    public static Specification<Product> build(String type, BigDecimal maxRate, Long bankId) {
        return Specification.where(typeEquals(type))
            .and(rateLessThan(maxRate))
            .and(bankEquals(bankId));
    }
}
