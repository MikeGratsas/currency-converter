package com.bankera.currencyconverter.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bankera.currencyconverter.entity.ExchangeRate;
import com.bankera.currencyconverter.exceptions.CurrencyNotFoundException;
import com.bankera.currencyconverter.exceptions.ExchangeRateNotFoundException;
import com.bankera.currencyconverter.exceptions.ExchangeRateUpdatedException;
import com.bankera.currencyconverter.form.ExchangeRateModel;
import com.bankera.currencyconverter.repository.ExchangeRateRepository;

@Service
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRateModel> listExchangeRates() {
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        return exchangeRateList.stream().map(ExchangeRateService::assembleExchangeRateModel).collect(Collectors.toList());
    }

    public List<ExchangeRateModel> listExchangeRatesByPage(final Pageable pageable) {
    	Page<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll(pageable);
        return exchangeRateList.stream().map(ExchangeRateService::assembleExchangeRateModel).collect(Collectors.toList());
    }

    public ExchangeRateModel createExchangeRate(String currencyCode, BigDecimal rate) {
        ExchangeRate exchangeRateEntity = new ExchangeRate();
        exchangeRateEntity.setCurrencyCode(currencyCode);
        exchangeRateEntity.setRate(rate);
        ExchangeRate c = exchangeRateRepository.save(exchangeRateEntity);
        return assembleExchangeRateModel(c);
    }

    public ExchangeRateModel createExchangeRate(ExchangeRateModel exchangeRateModel) {
        ExchangeRate exchangeRateEntity = new ExchangeRate();
        exchangeRateEntity.setCurrencyCode(exchangeRateModel.getCurrencyCode());
        exchangeRateEntity.setRate(exchangeRateModel.getRate());
        ExchangeRate c = exchangeRateRepository.save(exchangeRateEntity);
        return assembleExchangeRateModel(c);
    }

    public ExchangeRateModel saveExchangeRate(ExchangeRateModel exchangeRateModel) throws ExchangeRateNotFoundException, ExchangeRateUpdatedException {
        ExchangeRate exchangeRateEntity;
        Long id = exchangeRateModel.getId();
        if (id != null) {
            Optional<ExchangeRate> exchangeRateOptional = exchangeRateRepository.findById(id);
            if (exchangeRateOptional.isPresent()) {
                exchangeRateEntity = exchangeRateOptional.get();
                if (!exchangeRateEntity.getLastUpdated().equals(exchangeRateModel.getLastUpdated())) {
                    throw new ExchangeRateUpdatedException(id);
                }
            }
            else {
                throw new ExchangeRateNotFoundException(id);
            }
        }
        else {
        	final String currencyCode = exchangeRateModel.getCurrencyCode();
        	Optional<ExchangeRate> exchangeRateOptional = exchangeRateRepository.findByCurrencyCode(currencyCode);
            if (exchangeRateOptional.isPresent()) {
                exchangeRateEntity = exchangeRateOptional.get();
            }
            else {
            	exchangeRateEntity = new ExchangeRate();
            }
        }
        exchangeRateEntity.setCurrencyCode(exchangeRateModel.getCurrencyCode());
        exchangeRateEntity.setRate(exchangeRateModel.getRate());
        ExchangeRate c = exchangeRateRepository.save(exchangeRateEntity);
        return assembleExchangeRateModel(c);
    }

    public ExchangeRateModel updateExchangeRate(ExchangeRateModel exchangeRateModel) {
        ExchangeRate exchangeRateEntity;
    	final String currencyCode = exchangeRateModel.getCurrencyCode();
    	Optional<ExchangeRate> exchangeRateOptional = exchangeRateRepository.findByCurrencyCode(currencyCode);
        if (exchangeRateOptional.isPresent()) {
            exchangeRateEntity = exchangeRateOptional.get();
        }
        else {
        	exchangeRateEntity = new ExchangeRate();
            exchangeRateEntity.setCurrencyCode(currencyCode);
        }
        exchangeRateEntity.setRate(exchangeRateModel.getRate());
        ExchangeRate c = exchangeRateRepository.save(exchangeRateEntity);
        return assembleExchangeRateModel(c);
    }
    
    public ExchangeRateModel findExchangeRate(Long id) throws ExchangeRateNotFoundException {
        ExchangeRateModel exchangeRateModel = null;
        Optional<ExchangeRate> exchangeRateEntity = exchangeRateRepository.findById(id);
        if (exchangeRateEntity.isPresent()) {
            ExchangeRate c = exchangeRateEntity.get();
            exchangeRateModel = assembleExchangeRateModel(c);
        }
        else {
        	throw new ExchangeRateNotFoundException(id);
        }
        return exchangeRateModel;
    }

    public ExchangeRateModel findByCurrencyCode(String currencyCode) throws CurrencyNotFoundException {
        ExchangeRateModel exchangeRateModel = null;
        Optional<ExchangeRate> exchangeRateEntity = exchangeRateRepository.findByCurrencyCode(currencyCode);
        if (exchangeRateEntity.isPresent()) {
            ExchangeRate c = exchangeRateEntity.get();
            exchangeRateModel = assembleExchangeRateModel(c);
        }
        else {
    		throw new CurrencyNotFoundException(currencyCode);
        }
        return exchangeRateModel;
    }

    public void deleteExchangeRates(Long[] ids) {
        for (Long id: ids) {
            exchangeRateRepository.deleteById(id);
        }
    }

    private static ExchangeRateModel assembleExchangeRateModel(ExchangeRate exchangeRateEntity) {
        return new ExchangeRateModel(exchangeRateEntity.getId(), exchangeRateEntity.getCurrencyCode(), exchangeRateEntity.getRate().stripTrailingZeros(), exchangeRateEntity.getCreated(), exchangeRateEntity.getLastUpdated());
    }
}
