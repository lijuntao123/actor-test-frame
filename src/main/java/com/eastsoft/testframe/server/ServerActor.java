package com.eastsoft.testframe.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;

import com.eastsoft.testframe.model.RequestRegistry;
import com.eastsoft.testframe.model.RespRegistry;
import com.eastsoft.testframe.model.StartRun;
import com.eastsoft.testframe.model.SumResult;

/**
 * serverActor 接受 其他client连接并缓存，可通知所有连接的client同时开始发送请求
 * 
 * @author ljt
 * @date 2015-10-19 17:21:13
 *
 */
public class ServerActor extends UntypedActor {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ServerActor.class);
	public static Map<String, ActorRef> clientCatchMap = new HashMap<String, ActorRef>();
	private ActorRef displayResultActor;

	public ServerActor(ActorRef displayResultActor) {
		super();
		this.displayResultActor = displayResultActor;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof RequestRegistry) {
			RequestRegistry rr = (RequestRegistry) msg;			
			clientCatchMap.put(rr.getIp(), getSender());
			LOGGER.info("++++++++++client {} request registry,{} client has already connected", getSender()
					.path().address(),clientCatchMap.size());
			getContext().watch(getSender());
			RespRegistry respRegistry = new RespRegistry();
			respRegistry.setSuccess(true);
			respRegistry.setMessage("register success");
			getSender().tell(respRegistry, getSelf());
		} else if (msg instanceof Terminated) {
			Terminated ter = (Terminated) msg;
			LOGGER.info("---------client {} disconnected", ter.actor().path()
					.address());
			String key = ter.actor().path().address().host().get();
			clientCatchMap.remove(key);
		} else if (msg instanceof StartRun) {
			LOGGER.info("*************notice all of client start run");
			for (ActorRef ar : clientCatchMap.values()) {
				ar.tell(msg, getSelf());
			}
			LOGGER.info("************{} client has already notice",clientCatchMap.size());
		} else if (msg instanceof SumResult) {
			displayResultActor.tell(msg, getSender());
		}else{
			unhandled(msg);
		}
	}

}
