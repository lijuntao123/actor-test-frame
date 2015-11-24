package com.eastsoft.testframe.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.eastsoft.testframe.business.AbstractBusiness;
import com.eastsoft.testframe.model.Result;

/**
 * 作业actor，负责调用业务逻辑类，并将结果发送到统计actor中
 * @author ljt
 * @date 2015-10-19 14:05:14
 *
 */
public class WorkerActor extends UntypedActor{
	public static final Logger LOGGER = LoggerFactory
			.getLogger(WorkerActor.class);
	private Class targetClass;	
	private ActorRef collectActor;
	

	public WorkerActor(Class targetClass, ActorRef collectActor) {
		super();
		this.targetClass = targetClass;
		this.collectActor = collectActor;
	}


	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg instanceof Integer){
			Integer i=(Integer)msg;
			AbstractBusiness ab=(AbstractBusiness)targetClass.newInstance();
			Result result=ab.execute(i);
			collectActor.tell(result, getSelf());
		}else{
			unhandled(msg);
		}
		
				
	}

}
