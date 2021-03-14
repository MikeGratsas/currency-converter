package com.bankera.currencyconverter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankera.currencyconverter.entity.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
	Optional<ExchangeRate> findByCurrencyCode(String currencyCode);
}
