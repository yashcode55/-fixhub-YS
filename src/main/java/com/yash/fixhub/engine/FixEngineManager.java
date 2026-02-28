package com.yash.fixhub.engine;
import com.yash.fixhub.application.FixHubApplication;
import com.yash.fixhub.client.FixClientApplication;
import com.yash.fixhub.session.SessionRegistry;
import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixEngineManager {
	private static final Logger log =
	        LoggerFactory.getLogger(FixEngineManager.class);
    private SocketAcceptor acceptor;
    private SocketInitiator initiator;
    private SocketInitiator brokerInitiator;
    private final SessionRegistry sessionRegistry = new SessionRegistry();
    public void startServer() throws Exception {

        SessionSettings settings = new SessionSettings("fixhub.cfg");
        Application application = new FixHubApplication(sessionRegistry);

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        FileLogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        acceptor = new SocketAcceptor(
                application,
                storeFactory,
                settings,
                logFactory,
                messageFactory
        );

        acceptor.start();
        log.info("FIX Server started...");
    }

    public void startClient() throws Exception {

        SessionSettings settings = new SessionSettings("fixclient.cfg");
        Application application = new com.yash.fixhub.client.FixClientApplication();

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        FileLogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        initiator = new SocketInitiator(
                application,
                storeFactory,
                settings,
                logFactory,
                messageFactory
        );

        initiator.start();
        log.info("FIX Client started...");
    }
    
    public void startBroker() throws Exception {

        SessionSettings settings = new SessionSettings("fixbroker.cfg");

        Application application = new FixClientApplication(); // temporary reuse

        brokerInitiator = new SocketInitiator(
                application,
                new FileStoreFactory(settings),
                settings,
                new FileLogFactory(settings),
                new DefaultMessageFactory()
        );

        brokerInitiator.start();
        log.info("BROKER1 session started...");
    }
    
    public void stopAll() throws Exception {

        if (initiator != null) {
            initiator.stop();
        }

        if (acceptor != null) {
            acceptor.stop();
        }

        log.info("All FIX engines stopped.");
    }
}
