package com.n26.analyser.dto;

public class Statistics {

	double sum;
	double avg;
	double max;
	double min;
	long count;
	
	public Statistics() {
		
	}
	
	public Statistics(double sum, double avg, double max, double min, long count) {
		setSum(sum);
		setAvg(avg);
		setMax(max);
		setMin(min);
		setCount(count);
	}
	
	public void setSum(double sum) {
		this.sum = sum;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public void setCount(long count) {
		this.count = count;
	}

	public double getSum() {
		return sum;
	}

	public double getAvg() {
		return avg;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public long getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Statistics [sum=" + sum + ", avg=" + avg + ", max=" + max + ", min=" + min + ", count=" + count + "]";
	}
	
}
