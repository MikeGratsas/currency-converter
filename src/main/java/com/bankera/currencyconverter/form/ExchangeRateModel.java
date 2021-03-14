package com.bankera.currencyconverter.form;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

public class ExchangeRateModel {
	
    private Long id;

    @NotBlank(message = "{exchangeRate.currencyCode.required}")
    private String currencyCode;

	@Positive(message = "{exchangeRate.rate.positive}")
    @NotNull(message = "{exchangeRate.rate.required}")
    private BigDecimal rate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdated;

    public ExchangeRateModel() {
    }

    public ExchangeRateModel(String currencyCode, BigDecimal rate) {
        this.currencyCode = currencyCode;
        this.rate = rate;
    }

    public ExchangeRateModel(Long id, String currencyCode, BigDecimal rate, LocalDateTime created, LocalDateTime lastUpdated) {
        this.id = id;
        this.currencyCode = currencyCode;
        this.rate = rate;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRateModel that = (ExchangeRateModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(rate, that.rate) &&
                Objects.equals(created, that.created) &&
                Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyCode, rate, created, lastUpdated);
    }

	@Override
	public String toString() {
		return String.format("ExchangeRateModel [id=%s, currencyCode=%s, rate=%s, created=%s, lastUpdated=%s]", id,
				currencyCode, rate, created, lastUpdated);
	}
}
