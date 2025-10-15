package com.example.productservice.service;

import com.example.productservice.dto.OfferPageResponse;
import com.example.productservice.dto.OfferResponse;
import com.example.productservice.dto.ProductFilter;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.dto.BankDto;
import com.example.productservice.service.specification.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OfferService {

    private final ProductRepository productRepository;
    private final BankClient bankClient;

    public OfferService(ProductRepository productRepository, BankClient bankClient) {
        this.productRepository = productRepository;
        this.bankClient = bankClient;
    }

    public OfferPageResponse getOffers(ProductFilter filter, Pageable pageable) {
        Page<Product> page = productRepository.findAll(
            ProductSpecifications.build(filter.getType().orElse(null), filter.getMaxRate().orElse(null), filter.getBankId().orElse(null)),
            pageable);

        List<OfferResponse> offers = page.getContent().stream()
            .map(product -> {
                BankDto bank = bankClient.getBankById(product.getBankId());
                return new OfferResponse(product.getId(), product.getBankId(), bank.getName(), bank.getRating(),
                    product.getType(), product.getName(), product.getRate(), product.getTermMonths(), product.getCurrency());
            })
            .toList();

        return new OfferPageResponse(offers, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
