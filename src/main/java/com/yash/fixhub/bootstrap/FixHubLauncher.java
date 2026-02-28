package com.yash.fixhub.bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yash.fixhub.core.*;

	public class FixHubLauncher {
		
		private static final Logger log =
		        LoggerFactory.getLogger(FixHubApplication.class);
		
	    public static void main(String[] args) throws Exception {

	        FixEngineManager manager = new FixEngineManager();

	        manager.startServer();
	        manager.startClient();
	        manager.startBroker();

	        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	            try {
	                manager.stopAll();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }));

	        Thread.sleep(Long.MAX_VALUE);
	    }
}