package com.yash.fixhub.routing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;

public class RoutingEngine {
	private static final Logger log =
	        LoggerFactory.getLogger(RoutingEngine.class);
    public void route(Message message, SessionID sessionID) throws Exception {

    	log.info("Routing order...");

        // Temporary echo-back logic
        Session.sendToTarget(message, sessionID);
    }
}