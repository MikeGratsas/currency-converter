package com.bankera.currencyconverter.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankera.currencyconverter.exceptions.CurrencyNotFoundException;
import com.bankera.currencyconverter.form.CurrencyConversionModel;
import com.bankera.currencyconverter.form.ExchangeRateModel;

@Service
public class CurrencyConversionService {
    @Autowired
    private ExchangeRateService exchangeRateService;
    
    public CurrencyConversionModel convertCurrency(BigDecimal fromQuantity, String from, String to) throws CurrencyNotFoundException {
    	final ExchangeRateModel fromModel = exchangeRateService.findByCurrencyCode(from);
    	final ExchangeRateModel toModel = exchangeRateService.findByCurrencyCode(to);
    	return new CurrencyConversionModel(from, to, fromQuantity, fromQuantity.multiply(fromModel.getRate()).divide(toModel.getRate(), 18, RoundingMode.HALF_UP));
    }

}
