package com.yash.fixhub.routing;

import com.yash.fixhub.session.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;

public class RoutingEngine {

    private static final Logger log =
            LoggerFactory.getLogger(RoutingEngine.class);

    private final SessionRegistry sessionRegistry;

    public RoutingEngine(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void route(Message message, SessionID sourceSession) throws Exception {

        String targetBroker = "BROKER1"; // temporary hardcoded

        if (!sessionRegistry.isConnected(targetBroker)) {
            log.error("Target broker {} not connected!", targetBroker);
            return;
        }

        SessionID targetSession = sessionRegistry.getSession(targetBroker);

        log.info("Routing order from {} to {}",
                sourceSession,
                targetBroker);

        Session.sendToTarget(message, targetSession);
    }
}