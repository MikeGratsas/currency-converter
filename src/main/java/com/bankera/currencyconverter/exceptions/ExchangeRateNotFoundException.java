package com.bankera.currencyconverter.exceptions;

public class ExchangeRateNotFoundException extends DataEntityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2642486867184400846L;

	public ExchangeRateNotFoundException(Long id) {
		super("Exchange rate not found", id);
	}
}
