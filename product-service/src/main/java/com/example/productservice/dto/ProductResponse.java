package com.example.productservice.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private final Long id;
    private final Long bankId;
    private final String type;
    private final String name;
    private final BigDecimal rate;
    private final Integer termMonths;
    private final String currency;

    public ProductResponse(Long id, Long bankId, String type, String name, BigDecimal rate, Integer termMonths, String currency) {
        this.id = id;
        this.bankId = bankId;
        this.type = type;
        this.name = name;
        this.rate = rate;
        this.termMonths = termMonths;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public Long getBankId() {
        return bankId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public String getCurrency() {
        return currency;
    }
}
