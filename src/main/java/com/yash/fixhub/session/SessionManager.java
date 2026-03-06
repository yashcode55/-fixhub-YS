package com.yash.fixhub.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.SessionID;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final Logger log =
            LoggerFactory.getLogger(SessionManager.class);

    // compId -> SessionContext
    private final Map<String, SessionContext> sessionMap =
            new ConcurrentHashMap<>();

    /**
     * Register session metadata (before FIX logon)
     */
    public void addSession(SessionContext context) {
        sessionMap.put(context.getCompId(), context);
        log.info("Session metadata added for {}", context.getCompId());
    }

    /**
     * Retrieve session metadata
     */
    public SessionContext getSession(String compId) {
        return sessionMap.get(compId);
    }

    /**
     * Check if session is connected
     */
    public boolean isConnected(String compId) {
        SessionContext context = sessionMap.get(compId);
        return context != null && context.getSessionID() != null;
    }

    /**
     * Get all registered sessions
     */
    public Collection<SessionContext> getAllSessions() {
        return sessionMap.values();
    }

    /**
     * Optional: remove metadata completely
     */
    public void removeSession(String compId) {
        sessionMap.remove(compId);
        log.info("Session metadata removed for {}", compId);
    }
}