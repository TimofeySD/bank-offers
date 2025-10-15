package com.example.productservice.dto;

import java.math.BigDecimal;

public class OfferResponse {

    private final Long productId;
    private final Long bankId;
    private final String bankName;
    private final Integer bankRating;
    private final String type;
    private final String name;
    private final BigDecimal rate;
    private final Integer termMonths;
    private final String currency;

    public OfferResponse(Long productId, Long bankId, String bankName, Integer bankRating, String type, String name,
                         BigDecimal rate, Integer termMonths, String currency) {
        this.productId = productId;
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankRating = bankRating;
        this.type = type;
        this.name = name;
        this.rate = rate;
        this.termMonths = termMonths;
        this.currency = currency;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getBankId() {
        return bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public Integer getBankRating() {
        return bankRating;
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
