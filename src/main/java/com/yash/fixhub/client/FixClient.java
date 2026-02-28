package com.yash.fixhub.client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

public class  FixClient{
	private static final Logger log =
	        LoggerFactory.getLogger(FixClient.class);
	
public static void main(String[] args) throws Exception {

    SessionSettings settings = new SessionSettings("fixclient.cfg");

    Application application = new FixClientApplication();

    MessageStoreFactory storeFactory = new FileStoreFactory(settings);
    LogFactory logFactory = new FileLogFactory(settings);
    MessageFactory messageFactory = new DefaultMessageFactory();

    SocketInitiator initiator = new SocketInitiator(
            application,
            storeFactory,
            settings,
            logFactory,
            messageFactory
    );

    initiator.start();
    //System.out.println("FIX Client started...");
    log.info("FIX Client started...");
    
    // Keep application alive
    Thread.sleep(Long.MAX_VALUE);
	}
}