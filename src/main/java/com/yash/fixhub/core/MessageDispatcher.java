package com.yash.fixhub.core;

import com.yash.fixhub.routing.RoutingEngine;
import com.yash.fixhub.session.SessionManager;
import com.yash.fixhub.session.SessionRegistry;
import quickfix.Message;
import quickfix.SessionID;

public class MessageDispatcher {

    private final RoutingEngine routingEngine;

    public MessageDispatcher(SessionManager sessionManager) {
        this.routingEngine = new RoutingEngine(sessionManager);
    }

    public void dispatch(Message message, SessionID sessionID) throws Exception {
        routingEngine.route(message, sessionID);
    }
}