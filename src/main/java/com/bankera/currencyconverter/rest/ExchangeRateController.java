package com.bankera.currencyconverter.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.bankera.currencyconverter.exceptions.CurrencyNotFoundException;
import com.bankera.currencyconverter.exceptions.ExchangeRateNotFoundException;
import com.bankera.currencyconverter.exceptions.ExchangeRateUpdatedException;
import com.bankera.currencyconverter.form.ExchangeRateModel;
import com.bankera.currencyconverter.service.ExchangeRateService;
import com.bankera.currencyconverter.util.CsvHandler;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {

	@Autowired
	private ExchangeRateService exchangeRateService;

	@GetMapping(path = "/exchangerates/{id}")
	public ResponseEntity<ExchangeRateModel> getExchangeRateById(@PathVariable("id") final Long id) {
		ExchangeRateModel model;
		try {
			model = exchangeRateService.findExchangeRate(id);
		} catch (ExchangeRateNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e);
		}
		return new ResponseEntity<ExchangeRateModel>(model, HttpStatus.OK);
	}

	@GetMapping(path = "/exchangerates/currency/{currencyCode}")
	public ResponseEntity<ExchangeRateModel> getExchangeRateById(@PathVariable final String currencyCode) {
		ExchangeRateModel model;
		try {
			model = exchangeRateService.findByCurrencyCode(currencyCode);
		} catch (CurrencyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e);
		}
		return new ResponseEntity<ExchangeRateModel>(model, HttpStatus.OK);
	}

	@GetMapping(path = "/exchangerates")
	public ResponseEntity<List<ExchangeRateModel>> listExchangeRates(final Pageable pageable) {
		return new ResponseEntity<List<ExchangeRateModel>>(exchangeRateService.listExchangeRatesByPage(pageable),
				HttpStatus.OK);
	}

	@PostMapping("/exchangerates")
	public ResponseEntity<ExchangeRateModel> createExchangeRate(
			@Valid @RequestBody final ExchangeRateModel exchangeRateModel) {
		ExchangeRateModel model;
		try {
			model = exchangeRateService.createExchangeRate(exchangeRateModel);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getLocalizedMessage(), e);
		}
		return new ResponseEntity<ExchangeRateModel>(model, HttpStatus.CREATED);
	}

	@PutMapping(path = "/exchangerates")
	public ResponseEntity<ExchangeRateModel> updateExchangeRate(
			@Valid @RequestBody final ExchangeRateModel exchangeRateModel) {
		try {
			final ExchangeRateModel model = exchangeRateService.saveExchangeRate(exchangeRateModel);
			return new ResponseEntity<ExchangeRateModel>(model, HttpStatus.OK);
		} catch (ExchangeRateNotFoundException e) {
			return new ResponseEntity<ExchangeRateModel>(HttpStatus.NOT_FOUND);
		} catch (ExchangeRateUpdatedException e) {
			return new ResponseEntity<ExchangeRateModel>(HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping(path = "/exchangerates/{id}")
	public ResponseEntity<ExchangeRateModel> deleteExchangeRateById(@PathVariable("id") final Long id) {
		try {
			exchangeRateService.deleteExchangeRates(new Long[] { id });
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<ExchangeRateModel>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ExchangeRateModel>(HttpStatus.OK);
	}

	@GetMapping(path = "/exchangerates/export", produces = "text/csv")
	public void exportExchangeRatesCSV(final HttpServletResponse response) throws IOException {
		response.setContentType("text/csv; charset=UTF-8");
		final List<ExchangeRateModel> list = exchangeRateService.listExchangeRates();
		CsvHandler.writeExchangeRates(response.getWriter(), list);
	}

	@PostMapping(path = "/exchangerates/import", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ExchangeRateModel>> importExchangeRatesCSV(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		} else {

			try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
				final Stream<ExchangeRateModel> stream = CsvHandler.readExchangeRates(reader);
				final List<ExchangeRateModel> exchangeRates = stream
						.map(model -> exchangeRateService.updateExchangeRate(model)).collect(Collectors.toList());
				return new ResponseEntity<List<ExchangeRateModel>>(exchangeRates, HttpStatus.OK);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getLocalizedMessage(), e);
			}
		}
	}
}
