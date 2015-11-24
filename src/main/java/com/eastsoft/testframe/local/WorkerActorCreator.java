package com.eastsoft.testframe.local;

import akka.actor.ActorRef;
import akka.japi.Creator;

/**
 * 
 * @author ljt
 * @date 2015-10-19 15:39:40
 *
 */
public class WorkerActorCreator implements Creator<WorkerActor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3439099242164275893L;
	private Class targetClass;
	private ActorRef collectActor;

	public WorkerActorCreator(Class targetClass, ActorRef collectActor) {
		super();
		this.targetClass = targetClass;
		this.collectActor = collectActor;
	}

	public WorkerActor create() throws Exception {
		return new WorkerActor(targetClass,collectActor);
	}

}
