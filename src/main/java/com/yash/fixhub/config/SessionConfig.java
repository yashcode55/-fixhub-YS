package com.yash.fixhub.config;

public class SessionConfig {

    private String role;                 // BUY or SELL
    private String connectionType;       // ACCEPTOR or INITIATOR
    private String beginString;

    private String senderCompID;
    private String targetCompID;

    private String socketHost;
    private int socketPort;

    private String routingMode;          // STATIC or DYNAMIC
    private String defaultRoute;

    private int heartbeatInterval;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getConnectionType() { return connectionType; }
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }

    public String getBeginString() { return beginString; }
    public void setBeginString(String beginString) { this.beginString = beginString; }

    public String getSenderCompID() { return senderCompID; }
    public void setSenderCompID(String senderCompID) { this.senderCompID = senderCompID; }

    public String getTargetCompID() { return targetCompID; }
    public void setTargetCompID(String targetCompID) { this.targetCompID = targetCompID; }

    public String getSocketHost() { return socketHost; }
    public void setSocketHost(String socketHost) { this.socketHost = socketHost; }

    public int getSocketPort() { return socketPort; }
    public void setSocketPort(int socketPort) { this.socketPort = socketPort; }

    public String getRoutingMode() { return routingMode; }
    public void setRoutingMode(String routingMode) { this.routingMode = routingMode; }

    public String getDefaultRoute() { return defaultRoute; }
    public void setDefaultRoute(String defaultRoute) { this.defaultRoute = defaultRoute; }

    public int getHeartbeatInterval() { return heartbeatInterval; }
    public void setHeartbeatInterval(int heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
}