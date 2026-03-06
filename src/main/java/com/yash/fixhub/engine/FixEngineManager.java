package com.yash.fixhub.engine;

import com.yash.fixhub.application.FixHubApplication;
import com.yash.fixhub.broker.FixBrokerApplication;
import com.yash.fixhub.client.FixClientApplication;
import com.yash.fixhub.session.SessionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.*;

public class FixEngineManager {

    private static final Logger log =
            LoggerFactory.getLogger(FixEngineManager.class);

    private SocketAcceptor acceptor;
    private SocketInitiator clientInitiator;
    private SocketInitiator brokerInitiator;

    private final SessionManager sessionManager = new SessionManager();

    /**
     * Start HUB (Acceptor)
     */
    public void startServer() throws Exception {

        SessionSettings settings = new SessionSettings("fixhub.cfg");

        Application application = new FixHubApplication(sessionManager);

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        acceptor = new SocketAcceptor(
                application,
                storeFactory,
                settings,
                logFactory,
                messageFactory
        );

        acceptor.start();
        log.info("FIX HUB started...");
    }

    /**
     * Start CLIENT (Initiator)
     */
    public void startClient() throws Exception {

        SessionSettings settings = new SessionSettings("fixclient.cfg");

        Application application = new FixClientApplication();

        clientInitiator = new SocketInitiator(
                application,
                new FileStoreFactory(settings),
                settings,
                new FileLogFactory(settings),
                new DefaultMessageFactory()
        );

        clientInitiator.start();
        log.info("CLIENT session started...");
    }

    /**
     * Start BROKER (Initiator)
     */
    public void startBroker() throws Exception {

        SessionSettings settings = new SessionSettings("fixbroker.cfg");

        Application application = new FixBrokerApplication();

        brokerInitiator = new SocketInitiator(
                application,
                new FileStoreFactory(settings),
                settings,
                new FileLogFactory(settings),
                new DefaultMessageFactory()
        );

        brokerInitiator.start();
        log.info("BROKER session started...");
    }

    /**
     * Stop all FIX engines
     */
    public void stopAll() throws Exception {

        if (clientInitiator != null) {
            clientInitiator.stop();
        }

        if (brokerInitiator != null) {
            brokerInitiator.stop();
        }

        if (acceptor != null) {
            acceptor.stop();
        }

        log.info("All FIX engines stopped.");
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}