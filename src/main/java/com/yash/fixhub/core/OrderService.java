package com.yash.fixhub.core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Message;
import quickfix.SessionID;


import com.yash.fixhub.routing.RoutingEngine;

public class OrderService {
	private static final Logger log =
	        LoggerFactory.getLogger(FixHubApplication.class);
    private final RoutingEngine routingEngine;

    public OrderService() {
        this.routingEngine = new RoutingEngine();
    }

    public void handleNewOrder(Message message, SessionID sessionID) throws Exception {

    	log.info("Processing NewOrderSingle...");

        routingEngine.route(message, sessionID);
    }
}