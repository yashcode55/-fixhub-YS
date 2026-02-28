package com.yash.fixhub.core;

import com.yash.fixhub.routing.RoutingEngine;
import com.yash.fixhub.session.SessionRegistry;
import quickfix.Message;
import quickfix.SessionID;

public class OrderService {

    private final RoutingEngine routingEngine;

    public OrderService(SessionRegistry sessionRegistry) {
        this.routingEngine = new RoutingEngine(sessionRegistry);
    }

    public void handleNewOrder(Message message, SessionID sessionID) throws Exception {

        routingEngine.route(message, sessionID);
    }
}