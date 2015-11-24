package com.eastsoft.testframe.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestRegistry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8215358466780387732L;
	private String messgae;
	private Object remoter;
	private String ip;

	public Object getRemoter() {
		return remoter;
	}

	public void setRemoter(Object remoter) {
		this.remoter = remoter;
	}

	public String getMessgae() {
		return messgae;
	}

	public void setMessgae(String messgae) {
		this.messgae = messgae;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
