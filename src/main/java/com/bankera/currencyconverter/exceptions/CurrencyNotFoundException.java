package com.bankera.currencyconverter.exceptions;

public class CurrencyNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7445490747498576026L;

	private final String currencyCode;

	public CurrencyNotFoundException(String currencyCode) {
        super("Currency is not found: " + currencyCode);
        this.currencyCode = currencyCode;
    }

	public String getCurrencyCode() {
		return currencyCode;
	}
}
