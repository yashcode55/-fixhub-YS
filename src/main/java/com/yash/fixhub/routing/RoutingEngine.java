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

        // Identify who sent the message
        String sourceCompID = sourceSession.getTargetCompID();

        String targetCompID = null;

        // Routing logic
        if (sourceCompID.startsWith("CLIENT")) {
            targetCompID = "BROKER1";
        } else if (sourceCompID.startsWith("BROKER")) {
            log.info("Message originated from broker. Ignoring for now.");
            return;
        }

        if (targetCompID == null) {
            log.warn("No routing rule found for source {}", sourceCompID);
            return;
        }

        if (!sessionRegistry.isConnected(targetCompID)) {
            log.error("Target {} not connected!", targetCompID);
            return;
        }

        SessionID targetSession = sessionRegistry.getSession(targetCompID);

        log.info("Routing message from {} to {}",
                sourceCompID,
                targetCompID);

        Session.sendToTarget(message, targetSession);
    }
}