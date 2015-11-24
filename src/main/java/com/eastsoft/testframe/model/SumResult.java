package com.eastsoft.testframe.model;

import java.io.Serializable;

public class SumResult implements Serializable{
	private int numOfSum;
	private int numOfSuccess;
	private long totleTime;
	private long sumTime;
	private long maxTime;
	private long minTime;
	public int getNumOfSum() {
		return numOfSum;
	}
	public void setNumOfSum(int numOfSum) {
		this.numOfSum = numOfSum;
	}
	public int getNumOfSuccess() {
		return numOfSuccess;
	}
	public void setNumOfSuccess(int numOfSuccess) {
		this.numOfSuccess = numOfSuccess;
	}
	public long getTotleTime() {
		return totleTime;
	}
	public void setTotleTime(long totleTime) {
		this.totleTime = totleTime;
	}
	public long getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}
	public long getMinTime() {
		return minTime;
	}
	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}
	public long getSumTime() {
		return sumTime;
	}
	public void setSumTime(long sumTime) {
		this.sumTime = sumTime;
	}
	
	

}
