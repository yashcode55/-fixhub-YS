package com.yash.fixhub.service;

import com.yash.fixhub.config.SessionConfig;
import com.yash.fixhub.config.SessionConfigLoader;
import com.yash.fixhub.session.SessionContext;
import com.yash.fixhub.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

public class SessionBootstrapService {

    private static final Logger log =
            LoggerFactory.getLogger(SessionBootstrapService.class);

    private final SessionManager sessionManager;
    private final SessionConfigLoader configLoader = new SessionConfigLoader();

    // ONE settings object for all acceptor sessions
    private final SessionSettings acceptorSettings = new SessionSettings();

    // ONE settings object for all initiator sessions
    private final SessionSettings initiatorSettings = new SessionSettings();

    public SessionBootstrapService(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void loadAndRegister(String filePath) throws Exception {

        SessionConfig config = configLoader.load(filePath);

        SessionContext context = new SessionContext(
                config.getTargetCompID(),
                config.getRole(),
                config.getConnectionType(),
                config.getBeginString()
        );

        context.setDefaultRoute(config.getDefaultRoute());
        context.setRoutingMode(config.getRoutingMode());

        sessionManager.addSession(context);

        log.info("Registered session {} as {}",
                config.getTargetCompID(),
                config.getRole());

        addSessionSettings(config);

        printDebug(config, filePath);
    }

    private void addSessionSettings(SessionConfig config) throws Exception {

        SessionID sessionID = new SessionID(
                config.getBeginString(),
                config.getSenderCompID(),
                config.getTargetCompID()
        );

        SessionSettings target =
                "INITIATOR".equalsIgnoreCase(config.getConnectionType())
                        ? initiatorSettings
                        : acceptorSettings;

        target.setString(sessionID, "ConnectionType",
                config.getConnectionType().toLowerCase());

        target.setString(sessionID, "HeartBtInt",
                String.valueOf(config.getHeartbeatInterval()));

        target.setString(sessionID, "StartTime", "00:00:00");
        target.setString(sessionID, "EndTime", "23:59:59");

        target.setString(sessionID, "FileStorePath", "store");
        target.setString(sessionID, "FileLogPath", "log");

        if ("INITIATOR".equalsIgnoreCase(config.getConnectionType())) {

            target.setString(sessionID, "SocketConnectHost",
                    config.getSocketHost());

            target.setString(sessionID, "SocketConnectPort",
                    String.valueOf(config.getSocketPort()));

            target.setString(sessionID, "ReconnectInterval", "30");

        } else {

            target.setString(sessionID, "SocketAcceptPort",
                    String.valueOf(config.getSocketPort()));
        }
    }

    public void startEngines() throws Exception {

        Application application =
                new com.yash.fixhub.application.FixHubApplication(sessionManager);

        MessageFactory messageFactory = new DefaultMessageFactory();

        /*
         * START SINGLE ACCEPTOR
         * This will host multiple client sessions on same port
         */
        if (acceptorSettings.sectionIterator().hasNext()) {

            MessageStoreFactory storeFactory =
                    new FileStoreFactory(acceptorSettings);

            LogFactory logFactory =
                    new FileLogFactory(acceptorSettings);

            SocketAcceptor acceptor =
                    new SocketAcceptor(
                            application,
                            storeFactory,
                            acceptorSettings,
                            logFactory,
                            messageFactory
                    );

            acceptor.start();

            log.info("Shared SocketAcceptor started for all client sessions");
        }

        /*
         * START INITIATORS
         * Exchanges we connect OUTBOUND to
         */
        if (initiatorSettings.sectionIterator().hasNext()) {

            MessageStoreFactory storeFactory =
                    new FileStoreFactory(initiatorSettings);

            LogFactory logFactory =
                    new FileLogFactory(initiatorSettings);

            SocketInitiator initiator =
                    new SocketInitiator(
                            application,
                            storeFactory,
                            initiatorSettings,
                            logFactory,
                            messageFactory
                    );

            initiator.start();

            log.info("SocketInitiator started for exchange sessions");
        }
    }

    private void printDebug(SessionConfig config, String filePath) {

        log.info("===== FIX SESSION CONFIG LOADED =====");
        log.info("File: {}", filePath);
        log.info("Role: {}", config.getRole());
        log.info("ConnectionType: {}", config.getConnectionType());
        log.info("BeginString: {}", config.getBeginString());
        log.info("SenderCompID: {}", config.getSenderCompID());
        log.info("TargetCompID: {}", config.getTargetCompID());
        log.info("Host: {}", config.getSocketHost());
        log.info("Port: {}", config.getSocketPort());
        log.info("Heartbeat: {}", config.getHeartbeatInterval());
        log.info("RoutingMode: {}", config.getRoutingMode());
        log.info("DefaultRoute: {}", config.getDefaultRoute());
        log.info("=====================================");
    }
}