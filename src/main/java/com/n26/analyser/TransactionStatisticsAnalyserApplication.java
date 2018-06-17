package com.n26.analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransactionStatisticsAnalyserApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionStatisticsAnalyserApplication.class, args);
	}
}
