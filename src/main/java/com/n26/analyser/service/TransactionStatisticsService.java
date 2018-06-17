package com.n26.analyser.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.n26.analyser.dto.Statistics;
import com.n26.analyser.dto.Transaction;

@Service
public class TransactionStatisticsService {
	
	public static final Logger log = LoggerFactory.getLogger(TransactionStatisticsService.class);

	public static final long STATS_OLDER = 60000;
	public static final long FIXED_DELAY = 10;

	private List<Transaction> transactions = Collections.synchronizedList(new LinkedList<Transaction>());
	private Statistics statistics;

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public void register(Transaction transaction) {

		synchronized (transactions) {
			transactions.add(transaction);
		}

	}

	/*
	 * scheduled task with fixed delay runs to find the transactions in the last 60
	 * seconds and calculates the sum, avg, max, min, count in statistics. This task
	 * keeps running in the background and keeps statistics upto date. With the help
	 * of this scheduled task we are achieving memory O(1) to get statistics.
	 */
	@Scheduled(fixedDelay = FIXED_DELAY)
	public void statisticsCalculatorTask() {
		
		if (transactions.size() > 0) {
			List<Transaction> tempTransactions = new LinkedList<Transaction>();

			// fetches all the transactions present in the memory
			synchronized (transactions) {
				tempTransactions.addAll(transactions);
				transactions.clear();
			}
			// fetches the transactions from last 60 seconds
			List<Transaction> transactionsLast60Seconds = getTransactionsLast60Seconds(tempTransactions);
			if (transactionsLast60Seconds.size() > 0) {

				// generates the statistics for the transactions from last 60 seconds
				Statistics statisticsForTransactionsLast60Seconds = getStatisticsForTransactionsLast60Seconds(
						transactionsLast60Seconds);
				log.debug("setting the computed stats to statistics");
				setStatistics(statisticsForTransactionsLast60Seconds);

				synchronized (transactions) {
					transactions.addAll(tempTransactions);
					tempTransactions.clear();
				}
			} else {
				log.debug("setting to statistics to default values, as there are no transactions in last 60 seconds ");
				setStatistics(new Statistics());
			}

		} else {
			setStatistics(new Statistics());
		}

	}

	// method returns the transactions for last 60 seconds
	private List<Transaction> getTransactionsLast60Seconds(List<Transaction> transactions) {

		return transactions.parallelStream()
				.filter(trans -> (System.currentTimeMillis() - trans.getTimestamp()) < STATS_OLDER)
				.collect(Collectors.toList());
	}

	// generates statistics for the transaction in last 60 seconds
	private Statistics getStatisticsForTransactionsLast60Seconds(List<Transaction> transactions) {

		List<Double> amountsLast60Seconds = transactions.stream().map(Transaction::getAmount).collect(Collectors.toList());

		double sum = amountsLast60Seconds.stream().mapToDouble(Double::doubleValue).sum();
		double avg = amountsLast60Seconds.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
		double max = amountsLast60Seconds.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
		double min = amountsLast60Seconds.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
		long count = transactions.size();

		return new Statistics(sum, avg, max, min, count);

	}

}
