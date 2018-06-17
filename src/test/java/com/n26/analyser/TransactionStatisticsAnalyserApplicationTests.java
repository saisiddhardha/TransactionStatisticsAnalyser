package com.n26.analyser;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.analyser.dto.Statistics;
import com.n26.analyser.dto.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionStatisticsAnalyserApplicationTests {
	
	private static final Logger log = LoggerFactory.getLogger(TransactionStatisticsAnalyserApplicationTests.class);

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	public void shouldReturn200ForStatistics() throws Exception {
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
				"http://localhost:" + this.port + "/statistics", Map.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void shouldReturn204ForOldTransaction() throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		Transaction transaction = new Transaction();
		transaction.setAmount(50.0);
		transaction.setTimestamp(1529175877659l);
		
		HttpEntity<Transaction> requestBody = new HttpEntity<>(transaction, headers);
		
		ResponseEntity<String> entity = this.testRestTemplate.exchange("http://localhost:" + this.port + "/transactions", HttpMethod.POST, requestBody, String.class);
		then(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
	}
	
	@Test
	public void shouldReturn201ForTransactionLast60Seconds() throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		Transaction transaction = new Transaction();
		transaction.setAmount(100.0);
		transaction.setTimestamp(System.currentTimeMillis());
		
		HttpEntity<Transaction> requestBody = new HttpEntity<>(transaction, headers);
		
		ResponseEntity<String> entity = this.testRestTemplate.exchange("http://localhost:" + this.port + "/transactions", HttpMethod.POST, requestBody, String.class);
		then(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
	}

	@Test
	public void shouldReturnStatistics() throws Exception {
		
		ResponseEntity<Statistics> entity;
		
		//optional: just want to make sure schedule task in TransactionStatisticsService to complete execution atleast once
		Thread.sleep(1000);
		
		entity = this.testRestTemplate.getForEntity(
				"http://localhost:" + this.port + "/statistics", Statistics.class);
		
		log.info("Latest stats available ::" + entity.getBody().toString());
		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
}
