package com.bankera.currencyconverter.form;

import java.math.BigDecimal;

public class CurrencyConversionModel {
	private String from;
	private String to;
	private BigDecimal quantity;
	private BigDecimal totalCalculatedAmount;
	
	public CurrencyConversionModel(String from, String to, BigDecimal quantity, BigDecimal totalCalculatedAmount) {
		this.from = from;
		this.to = to;
		this.quantity = quantity;
		this.totalCalculatedAmount = totalCalculatedAmount;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalCalculatedAmount() {
		return totalCalculatedAmount;
	}

	public void setTotalCalculatedAmount(BigDecimal totalCalculatedAmount) {
		this.totalCalculatedAmount = totalCalculatedAmount;
	}

}
