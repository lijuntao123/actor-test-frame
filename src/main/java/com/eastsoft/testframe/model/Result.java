package com.eastsoft.testframe.model;

import java.io.Serializable;

public class Result implements Serializable {
	private long costTime;
	private boolean success;

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "Result [costTime=" + costTime + ", success=" + success + "]";
	}
	
	

}
