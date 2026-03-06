package com.yash.fixhub.session;

import quickfix.SessionID;

public class SessionContext {

    private final String compId;
    private final String role;              // BUY or SELL
    private final String connectionType;    // ACCEPTOR or INITIATOR
    private final String fixVersion;        // FIX.4.2 / FIX.4.4

    private String routingMode;             // STATIC / DYNAMIC
    private String defaultRoute;            // Sell side compId

    private SessionID sessionID;

    public SessionContext(String compId,
                          String role,
                          String connectionType,
                          String fixVersion) {

        this.compId = compId;
        this.role = role;
        this.connectionType = connectionType;
        this.fixVersion = fixVersion;
    }

    public String getCompId() {
        return compId;
    }

    public String getRole() {
        return role;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public String getRoutingMode() {
        return routingMode;
    }

    public void setRoutingMode(String routingMode) {
        this.routingMode = routingMode;
    }

    public String getDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(String defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    public SessionID getSessionID() {
        return sessionID;
    }

    public void setSessionID(SessionID sessionID) {
        this.sessionID = sessionID;
    }
}