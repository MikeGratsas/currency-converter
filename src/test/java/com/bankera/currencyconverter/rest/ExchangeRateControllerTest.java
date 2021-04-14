package com.bankera.currencyconverter.rest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bankera.currencyconverter.form.ExchangeRateModel;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeRateControllerTest {

    @Autowired
	private TestRestTemplate template;
    
    @BeforeEach
	void setUp() throws Exception {
    	Assertions.assertNotNull(template);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    void testExchangeRate()
    {
		HttpEntity<Object> exchangeRateEntity = getHttpEntity("{\"currencyCode\": \"RUB\", \"rate\": \"-0.01138952164\" }");
		ResponseEntity<ExchangeRateModel> response = template.postForEntity("/api/exchangerates", exchangeRateEntity, ExchangeRateModel.class);
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		exchangeRateEntity = getHttpEntity("{\"currencyCode\": \"RUB\", \"rate\": \"0.01138952164\" }");
		response = template.postForEntity("/api/exchangerates", exchangeRateEntity, ExchangeRateModel.class);
		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		final ExchangeRateModel model = response.getBody();
		final Long id = model.getId();
		final String currencyCode = model.getCurrencyCode();
		Assertions.assertNotNull(id);
		Assertions.assertEquals("RUB", currencyCode);

		response = template.postForEntity("/api/exchangerates", exchangeRateEntity, ExchangeRateModel.class);
		Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());

		response = template.getForEntity("/api/exchangerates/{id}", ExchangeRateModel.class, id);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(id, response.getBody().getId());

		response = template.getForEntity("/api/exchangerates/currency/{currencyCode}", ExchangeRateModel.class, currencyCode);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(currencyCode, response.getBody().getCurrencyCode());

		ResponseEntity<ExchangeRateModel[]> resultList = template.getForEntity("/api/exchangerates", ExchangeRateModel[].class);
		Assertions.assertEquals(HttpStatus.OK, resultList.getStatusCode());
		Assertions.assertTrue(resultList.getBody().length > 0);

		exchangeRateEntity = getHttpEntity("{\"currencyCode\": \"RUB\", \"rate\": \"0.01139521647\" }");
		response = template.exchange("/api/exchangerates", HttpMethod.PUT, exchangeRateEntity, ExchangeRateModel.class);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

		exchangeRateEntity = getHttpEntity("{\"id\": \"0\",\"currencyCode\": \"RUB\", \"rate\": \"0.01139521647\" }");
		response = template.exchange("/api/exchangerates", HttpMethod.PUT, exchangeRateEntity, ExchangeRateModel.class);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		template.delete("/api/exchangerates/{id}", id);

		ResponseEntity<Long> result = template.exchange("/api/exchangerates/{id}", HttpMethod.DELETE, null, Long.class, id);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

		response = template.getForEntity("/api/exchangerates/{id}", ExchangeRateModel.class, id);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		response = template.getForEntity("/api/exchangerates/currency/{currencyCode}", ExchangeRateModel.class, currencyCode);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
     
	private HttpEntity<Object> getHttpEntity(Object body) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(body, headers);
	}
}
