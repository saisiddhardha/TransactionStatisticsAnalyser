package com.n26.analyser.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.analyser.dto.Statistics;
import com.n26.analyser.dto.Transaction;
import com.n26.analyser.service.TransactionStatisticsService;

@RestController
public class TransactionStatisticsController {

	@Autowired
	private TransactionStatisticsService transactionStatisticsService;

	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<String> registerTransaction(@Valid @NotNull @RequestBody Transaction transaction) {

		if (System.currentTimeMillis() - transaction.getTimestamp() < TransactionStatisticsService.STATS_OLDER) {
			transactionStatisticsService.register(transaction);
			return new ResponseEntity<>("", HttpStatus.CREATED);
		} else {
			// transactions older than 60 seconds, sending empty response body
			return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
		}

	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public ResponseEntity<?> getStatistics() {
		// returns the statistics from TransactionStatisticsService
		return new ResponseEntity<Statistics>(transactionStatisticsService.getStatistics(), HttpStatus.OK);
	}

}
