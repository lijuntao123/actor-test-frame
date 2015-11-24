package com.eastsoft.testframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

import com.eastsoft.testframe.business.AbstractBusiness;
import com.eastsoft.testframe.local.CollectActor;
import com.eastsoft.testframe.local.WorkerActorCreator;
import com.eastsoft.testframe.model.RequestRegistry;
import com.eastsoft.testframe.model.RespRegistry;
import com.eastsoft.testframe.model.StartRun;
import com.eastsoft.testframe.server.DisplayResultActor;
import com.eastsoft.testframe.server.ServerActor;
import com.typesafe.config.ConfigFactory;

/**
 * Hello world!
 *
 */
public class StartUp extends UntypedActor {
	public static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);
	private static int numOfWorker;
	private static int numOfTotal;
	private static String bussinessClassPath;
	private static String bussinessJarPath;
	private static String serverHost;
	private static int serverPort;
	private static ActorSystem system;
	private static boolean isServer;
	private static ActorRef remote = null;
	private static ActorRef displayActor = null;
	private static String localHost;
	private TimerTask task;
	private Timer timer;

	public static void init() {
		isServer = ConfigFactory.load().getBoolean("isServer");
		numOfWorker = ConfigFactory.load().getInt("parameter.numOfWorker");
		numOfTotal = ConfigFactory.load().getInt("parameter.numOfTotal");
		bussinessClassPath = ConfigFactory.load().getString(
				"bussiness.classPath");
		localHost = ConfigFactory.load().getString("localHost.hostName");
		bussinessJarPath = ConfigFactory.load().getString("bussiness.jarPath");
		serverHost = ConfigFactory.load().getString("remoteServer.hostName");
		serverPort = ConfigFactory.load().getInt("remoteServer.port");
		if (isServer) {
			system = ActorSystem.create("ServerSys", ConfigFactory.load()
					.getConfig("ServerSys"));
			displayActor = system.actorOf(Props
					.create(DisplayResultActor.class));
			remote = system.actorOf(Props.create(ServerActor.class,
					new Object[] { displayActor }),"serverActor");
		} else {
			system = ActorSystem.create("localSys", ConfigFactory.load()
					.getConfig("localSys"));
			String addr = "akka.tcp://ServerSys@" + serverHost + ":"
					+ serverPort + "/user/serverActor";
			remote = system.actorFor(addr);

		}

	}

	private void exec(int numOfTotal,int numOfWorker) {
		ClassLoader classLoader = null;
		if (!"".equals(bussinessJarPath)) {
			File xFile = new File(bussinessJarPath);
			URL xUrl = null;
			try {
				xUrl = xFile.toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			classLoader = new URLClassLoader(new URL[] { xUrl });
		} else {

		}
		classLoader = this.getClass().getClassLoader();
		Class xClass = null;
		try {
			xClass = classLoader.loadClass(bussinessClassPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		ActorRef collectActor = system.actorOf(Props.create(CollectActor.class,
				new Object[] { numOfTotal, remote }));
		ActorRef router = system.actorOf(Props.create(
				new WorkerActorCreator(xClass, collectActor)).withRouter(
				new RoundRobinRouter(numOfWorker)));

		for (int i = 0; i < numOfTotal; i++) {
			router.tell(i, null);
		}

	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if ("setup".equals(msg)) {
			final RequestRegistry rr = new RequestRegistry();
			rr.setIp(localHost);			
			task = new TimerTask() {

                @Override
                public void run() {
                    LOGGER.info("++++++send registry message {}", new Date());
                    remote.tell(rr, getSelf());

                }
            };
            timer = new Timer();
            timer.schedule(task,0, 10 * 1000);
		} else if (msg instanceof RespRegistry) {
			RespRegistry rr = (RespRegistry) msg;
			LOGGER.info("+++++register success:{}", rr.isSuccess());
			getContext().watch(getSender());
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
		} else if (msg instanceof Terminated) {
            LOGGER.info("Terminated  " + msg.toString());
            final ActorRef actor = getSelf();
            getSelf().tell("setup", actor);

        } else if (msg instanceof StartRun) {
        	StartRun sr=(StartRun)msg;
			exec(sr.getNumOfTotal(),sr.getNumOfWorker());
		}else if("completed".equals(msg)){
			LOGGER.info(
					"current has {} client already connected, whether start test?( input yes start otherwise donnot start!)",
					ServerActor.clientCatchMap.size());
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String inputStr = br.readLine();
			if ("yes".equalsIgnoreCase(inputStr)) {
				remote.tell("run", remote.noSender());
			}else if("quit".equalsIgnoreCase(inputStr)){
				system.shutdown();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		init();
		ActorRef actor = system.actorOf(Props.create(StartUp.class));
		actor.tell("setup", actor.noSender());
		
		if (isServer) {
//			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LOGGER.info(
						"current has {} client already connected, whether start test?( input yes start otherwise donnot start!)",
						ServerActor.clientCatchMap.size());
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				String inputStr = br.readLine();
				if ("yes".equalsIgnoreCase(inputStr)) {
					StartRun sr=new StartRun();
					sr.setNumOfTotal(numOfTotal);
					sr.setNumOfWorker(numOfWorker);
					remote.tell(sr, remote.noSender());
				}else if("quit".equalsIgnoreCase(inputStr)){
					system.shutdown();
				}
//			}
		}
		
//		system.shutdown();
	}
}
