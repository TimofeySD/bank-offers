package com.example.productservice.service;

import com.example.productservice.service.dto.BankDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bank-service", url = "${bank.service.url:http://localhost:8081}")
public interface BankClient {

    @GetMapping("/api/v1/banks/{id}")
    BankDto getBankById(@PathVariable("id") Long id);
}
