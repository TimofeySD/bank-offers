package com.example.productservice.controller;

import com.example.productservice.dto.OfferPageResponse;
import com.example.productservice.dto.ProductFilter;
import com.example.productservice.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/offers")
@Tag(name = "Offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    @Operation(summary = "Aggregated offers with bank details")
    public OfferPageResponse getOffers(@RequestParam(required = false) String type,
                                       @RequestParam(required = false) BigDecimal maxRate,
                                       @RequestParam(required = false) Long bankId,
                                       @PageableDefault(size = 20) Pageable pageable) {
        ProductFilter filter = new ProductFilter(type, maxRate, bankId);
        return offerService.getOffers(filter, pageable);
    }
}
