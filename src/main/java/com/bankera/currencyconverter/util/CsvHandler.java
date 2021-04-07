package com.bankera.currencyconverter.util;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.bankera.currencyconverter.form.ExchangeRateModel;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvException;

public class CsvHandler {
	
	private static final Logger LOGGER = Logger.getLogger(CsvHandler.class.getName());

	private CsvHandler() {
	}

	public static Stream<ExchangeRateModel> readExchangeRates(Reader reader) {
        ColumnPositionMappingStrategy<ExchangeRateModel> mapStrategy
		        = new ColumnPositionMappingStrategy<>();

		mapStrategy.setType(ExchangeRateModel.class);

		String[] columns = new String[]{"currencyCode", "rate"};
		mapStrategy.setColumnMapping(columns);

		CsvToBean<ExchangeRateModel> csvToBean = new CsvToBeanBuilder<ExchangeRateModel>(reader)
		        .withMappingStrategy(mapStrategy)
		        .withSkipLines(0)
		        .withIgnoreLeadingWhiteSpace(true)
		        .build();
		
		return csvToBean.stream();
    }

    public static void writeExchangeRates(Writer writer, List<ExchangeRateModel> exchangeRates) {

        try {

            ColumnPositionMappingStrategy<ExchangeRateModel> mapStrategy
                    = new ColumnPositionMappingStrategy<>();

            mapStrategy.setType(ExchangeRateModel.class);

            String[] columns = new String[]{"currencyCode", "rate"};
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv<ExchangeRateModel> btcsv = new StatefulBeanToCsvBuilder<ExchangeRateModel>(writer)
                    .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(ICSVWriter.DEFAULT_SEPARATOR)
                    .withMappingStrategy(mapStrategy)
                    .build();

            btcsv.write(exchangeRates);

        }
        catch (CsvException ex) {
            LOGGER.log(Level.WARNING, "Error mapping Bean to CSV", ex);
        }
    }
}
