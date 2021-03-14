package com.bankera.currencyconverter.exceptions;

public class ExchangeRateUpdatedException extends DataEntityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -382806181879041360L;

	public ExchangeRateUpdatedException(Long id) {
		super("Exchange rate was updated since last read", id);
	}
}
