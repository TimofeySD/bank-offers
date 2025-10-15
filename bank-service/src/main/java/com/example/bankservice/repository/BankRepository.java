package com.example.bankservice.repository;

import com.example.bankservice.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findByNameIgnoreCase(String name);
}
