package com.yash.fixhub.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SessionConfigLoader {

    public SessionConfig load(String filePath) throws IOException {

        Properties props = new Properties();
        props.load(new FileInputStream(filePath));

        SessionConfig config = new SessionConfig();

        config.setRole(props.getProperty("Role"));
        config.setConnectionType(props.getProperty("ConnectionType"));
        config.setBeginString(props.getProperty("BeginString"));

        config.setSenderCompID(props.getProperty("SenderCompID"));
        config.setTargetCompID(props.getProperty("TargetCompID"));

        // ACCEPTOR PORT
        String acceptPort = props.getProperty("SocketAcceptPort");
        if (acceptPort != null) {
            config.setSocketPort(Integer.parseInt(acceptPort));
        }

        // INITIATOR HOST
        String host = props.getProperty("SocketConnectHost");
        if (host != null) {
            config.setSocketHost(host);
        }

        // INITIATOR PORT
        String connectPort = props.getProperty("SocketConnectPort");
        if (connectPort != null) {
            config.setSocketPort(Integer.parseInt(connectPort));
        }

        config.setRoutingMode(props.getProperty("RoutingMode"));
        config.setDefaultRoute(props.getProperty("DefaultRoute"));

        config.setHeartbeatInterval(
                Integer.parseInt(props.getProperty("HeartbeatInterval", "30"))
        );

        return config;
    }
}