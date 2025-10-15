package com.example.productservice.dto;

import java.util.List;

public class OfferPageResponse {

    private final List<OfferResponse> items;
    private final long totalElements;
    private final int page;
    private final int size;

    public OfferPageResponse(List<OfferResponse> items, long totalElements, int page, int size) {
        this.items = items;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }

    public List<OfferResponse> getItems() {
        return items;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
