package com.yash.fixhub.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.SessionID;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {

    private static final Logger log =
            LoggerFactory.getLogger(SessionRegistry.class);

    // Key = logical counterparty CompID
    private final Map<String, SessionID> sessionMap = new ConcurrentHashMap<>();

    public void register(SessionID sessionID) {

        String counterpartyCompID;

        // If we are acceptor, counterparty is TargetCompID
        if ("FIXHUB".equals(sessionID.getSenderCompID())) {
            counterpartyCompID = sessionID.getTargetCompID();
        } else {
            counterpartyCompID = sessionID.getSenderCompID();
        }

        sessionMap.put(counterpartyCompID, sessionID);

        log.info("Registered session for counterparty: {}", counterpartyCompID);
    }

    public void deregister(SessionID sessionID) {

        String counterpartyCompID;

        if ("FIXHUB".equals(sessionID.getSenderCompID())) {
            counterpartyCompID = sessionID.getTargetCompID();
        } else {
            counterpartyCompID = sessionID.getSenderCompID();
        }

        sessionMap.remove(counterpartyCompID);

        log.info("Deregistered session for counterparty: {}", counterpartyCompID);
    }

    public SessionID getSession(String compID) {
        return sessionMap.get(compID);
    }

    public boolean isConnected(String compID) {
        return sessionMap.containsKey(compID);
    }
}