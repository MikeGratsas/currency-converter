package com.bankera.currencyconverter.rest;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bankera.currencyconverter.exceptions.CurrencyNotFoundException;
import com.bankera.currencyconverter.form.CurrencyConversionModel;
import com.bankera.currencyconverter.service.CurrencyConversionService;

@RestController
@RequestMapping("/api")
public class CurrencyConversionController {

	@Autowired
	private CurrencyConversionService currencyConversionService;

	@GetMapping("/currency-converter/from/{from}/to/{to}")
	public ResponseEntity<BigDecimal> convertCurrency(@PathVariable String from, @PathVariable String to,
			@RequestParam BigDecimal quantity) {
		CurrencyConversionModel model;
		try {
			model = currencyConversionService.convertCurrency(quantity, from, to);
		} catch (CurrencyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e);
		}
		return ResponseEntity.ok(model.getTotalCalculatedAmount());
	}

}
