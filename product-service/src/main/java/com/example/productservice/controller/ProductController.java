package com.example.productservice.controller;

import com.example.productservice.dto.ProductFilter;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Search products")
    public Page<ProductResponse> searchProducts(@RequestParam(required = false) String type,
                                                @RequestParam(required = false) BigDecimal maxRate,
                                                @RequestParam(required = false) Long bankId,
                                                @PageableDefault(size = 20) Pageable pageable) {
        ProductFilter filter = new ProductFilter(type, maxRate, bankId);
        return productService.findProducts(filter, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create product")
    public ProductResponse createProduct(@Validated @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.findById(id);
    }
}
