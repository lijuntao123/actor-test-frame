package com.eastsoft.testframe.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eastsoft.testframe.model.Result;

public abstract class AbstractBusiness {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractBusiness.class);
	private long startTime;

	protected abstract void prepare();

	protected abstract boolean doWork();

	protected abstract void clean();

	public final Result execute(int index) {
		prepare();
		Result result = new Result();
		startTime = System.currentTimeMillis();
		boolean r = doWork();
		long costTime = System.currentTimeMillis() - startTime;
		result.setCostTime(costTime);
		result.setSuccess(r);
		LOGGER.info(" the {}ed doWorker success :{} ,cost time is :{} ms",index,
				result, costTime);
		clean();

		return result;

	}

}
