package com.eastsoft.testframe.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eastsoft.testframe.StartUp;
import com.eastsoft.testframe.model.SumResult;
import com.eastsoft.testframe.util.ConsoleTable;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * display the last test result
 * 
 * @author ljt
 * @date 2015-10-19 17:41:44
 *
 */
public class DisplayResultActor extends UntypedActor {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(DisplayResultActor.class);
	private int numOfClient = 0;
	private int numOfTotal = 0;
	private int numOfSuccess = 0;
	private long startTime = 0;
	private long sumTime = 0;
	private long maxTime = 0;
	private long minTime = 0;

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof SumResult) {
			SumResult sumResult = (SumResult) msg;
			if(numOfClient==0){
				startTime =System.currentTimeMillis()-sumResult.getTotleTime();
			}
			
			String[] array1 = { sumResult.getNumOfSum() + "", sumResult.getTotleTime() + "ms",
					sumResult.getMaxTime() + "ms", sumResult.getMinTime() + "ms", (float)sumResult.getSumTime()/(float)sumResult.getNumOfSum()+"ms",sumResult.getNumOfSuccess() + "",
					(sumResult.getNumOfSum() - sumResult.getNumOfSuccess()) + "",
					(float) sumResult.getNumOfSuccess() / (float) sumResult.getNumOfSum()*100 + "%" };
			String s1=ConsoleTable.printResult(array1);
			LOGGER.info("---from {} test result as follows \n{}",getSender().path().address(),s1);
			
			
			sumTime+=sumResult.getSumTime();
			numOfClient++;
			numOfTotal += sumResult.getNumOfSum();
			numOfSuccess += sumResult.getNumOfSuccess();
			
			if (maxTime != 0) {
				if (maxTime < sumResult.getMaxTime()) {
					maxTime = sumResult.getMaxTime();
				}
			} else {
				maxTime = sumResult.getMaxTime();
			}

			if (minTime != 0) {
				if (minTime < sumResult.getMinTime()) {
					minTime = sumResult.getMinTime();
				}
			} else {
				minTime = sumResult.getMinTime();
			}

			if (numOfClient == ServerActor.clientCatchMap.size()) {
				long costTime=System.currentTimeMillis()-startTime;
				LOGGER.info("------------------test end ,result as follows------------------");
				String[] array = { numOfTotal + "", costTime + "ms",
						maxTime + "ms", minTime + "ms", (double)sumTime/(double)numOfTotal+"ms",numOfSuccess + "",
						(numOfTotal - numOfSuccess) + "",
						(float) numOfSuccess / (float) numOfTotal*100 + "%" };
				String str2=ConsoleTable.printResult(array);
				LOGGER.info("---the {} all test has completed, result as follows \n{}",numOfClient,str2);
				ActorRef actor = getContext().actorOf(Props.create(StartUp.class));
				actor.tell("completed", actor.noSender());
				numOfClient = 0;
				numOfTotal = 0;
				numOfSuccess = 0;
				startTime = 0;
				maxTime = 0;
				minTime = 0;
				sumTime=0;
			}
		}

	}

}
