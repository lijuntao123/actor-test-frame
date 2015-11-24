/**
 * 
 */
package com.eastsoft.testframe.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eastsoft.testframe.business.AbstractBusiness;
import com.eastsoft.testframe.model.Result;
import com.eastsoft.testframe.model.SumResult;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * 汇总结果，并将结果发送到远端
 * 
 * @author ljt
 * @date 2015-10-19 14:40:45
 *
 */
public class CollectActor extends UntypedActor {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(CollectActor.class);
	private final int numOfSum;
	private ActorRef remote;
	private long startTime = 0;
	private long sumTime=0;
	private SumResult sumResult;
	private int count = 0;
	private int numOfSuccess = 0;

	public CollectActor(int numSum, ActorRef remote) {
		super();
		numOfSum = numSum;
		this.remote = remote;
		sumResult = new SumResult();
		sumResult.setNumOfSum(numOfSum);
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Result) {
			Result result = (Result) msg;
			if(count==0){
				startTime =System.currentTimeMillis()- result.getCostTime();
			}
			count++;
			sumTime+=result.getCostTime();
			if (sumResult.getMaxTime() != 0) {
				if (result.getCostTime() > sumResult.getMaxTime()) {
					sumResult.setMaxTime(result.getCostTime());
				}
			} else {
				sumResult.setMaxTime(result.getCostTime());
			}

			if (sumResult.getMinTime() != 0) {
				if (result.getCostTime() < sumResult.getMinTime()) {
					sumResult.setMinTime(result.getCostTime());
				}
			} else {
				sumResult.setMinTime(result.getCostTime());
			}

			if (result.isSuccess()) {
				numOfSuccess++;
			}

			if (count == numOfSum) {
				long costTime=System.currentTimeMillis()-startTime;
				LOGGER.info(
						"---{} request has already all complete,success num :{} ,cost time :{}ms",
						numOfSum, numOfSuccess, costTime);
				sumResult.setTotleTime(costTime);
				sumResult.setSumTime(sumTime);
				sumResult.setNumOfSuccess(numOfSuccess);
				remote.tell(sumResult, getSelf());
				
				count=0;
				startTime=0;
				numOfSuccess=0;
				sumTime=0;
			}

		}else{
			unhandled(msg);
		}

	}

}
