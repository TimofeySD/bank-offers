package com.example.bankservice.service;

import com.example.bankservice.dto.BankRequest;
import com.example.bankservice.dto.BankResponse;
import com.example.bankservice.entity.Bank;
import com.example.bankservice.repository.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<BankResponse> findAll() {
        return bankRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public BankResponse create(BankRequest request) {
        Bank bank = new Bank();
        bank.setName(request.getName());
        bank.setRating(request.getRating());
        bank.setCountry(request.getCountry());
        return toResponse(bankRepository.save(bank));
    }

    @Transactional(readOnly = true)
    public BankResponse findById(Long id) {
        return bankRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Bank not found: " + id));
    }

    private BankResponse toResponse(Bank bank) {
        return new BankResponse(bank.getId(), bank.getName(), bank.getRating(), bank.getCountry());
    }
}
