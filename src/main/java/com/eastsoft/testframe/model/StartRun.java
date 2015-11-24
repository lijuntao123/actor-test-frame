package com.eastsoft.testframe.model;

import java.io.Serializable;

/**
 * 
 * @author ljt
 * @date 2015-10-21 17:46:37
 *
 */
public class StartRun implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3546575896771666186L;
	private int numOfTotal;
	private int numOfWorker;

	public int getNumOfTotal() {
		return numOfTotal;
	}

	public void setNumOfTotal(int numOfTotal) {
		this.numOfTotal = numOfTotal;
	}

	public int getNumOfWorker() {
		return numOfWorker;
	}

	public void setNumOfWorker(int numOfWorker) {
		this.numOfWorker = numOfWorker;
	}

}
