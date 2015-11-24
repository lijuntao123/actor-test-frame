package com.eastsoft.testframe.model;

import java.io.Serializable;

/**
 * Created by eastsoft_eastsoft on 2015/10/8.
 */
public class RespRegistry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 138569049609077180L;
	private boolean success;
	private String message;
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RespRegistry{" + "success=" + success + ", message='" + message
				+ '\'' + '}';
	}

	

}
