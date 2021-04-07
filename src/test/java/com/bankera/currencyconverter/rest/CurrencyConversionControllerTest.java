package com.bankera.currencyconverter.rest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.bankera.currencyconverter.form.ExchangeRateModel;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyConversionControllerTest {

    @Autowired
	private TestRestTemplate template;
    
    @BeforeEach
	void setUp() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
		requestMap.add("file", new ClassPathResource("csv/rates.csv"));

		final ParameterizedTypeReference<List<ExchangeRateModel>> typeReference = new ParameterizedTypeReference<List<ExchangeRateModel>>() {
		};

		final ResponseEntity<List<ExchangeRateModel>> response = template.exchange("/api/exchangerates/import", HttpMethod.POST, new HttpEntity<>(requestMap, headers), typeReference);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(6, response.getBody().size());

		final ResponseEntity<String> csvResponse = template.getForEntity("/api/exchangerates/export", String.class);
		Assertions.assertEquals(HttpStatus.OK, csvResponse.getStatusCode());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    void testConvertCurrency()
    {
        ResponseEntity<BigDecimal> response = template.getForEntity("/api/currency-converter/from/{from}/to/{to}?quantity={quantity}", BigDecimal.class, "EUR", "USD", 10000);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(18, response.getBody().scale());

        response = template.getForEntity("/api/currency-converter/from/{from}/to/{to}?quantity={quantity}", BigDecimal.class, "USD", "BTC", 10000);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(18, response.getBody().scale());
    }
}
