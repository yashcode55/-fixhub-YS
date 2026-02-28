package com.yash.fixhub.core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

public class FixHubServer {
	private static final Logger log =
	        LoggerFactory.getLogger(FixHubServer.class);
    private static SocketAcceptor acceptor;

    public static void main(String[] args) throws Exception {

        Application application = new FixHubApplication();

        SessionSettings settings = new SessionSettings("fixhub.cfg");
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
        log.info("FIX Hub started on port 9878...");

        // 🔥 Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
            	log.info("Stopping FIX Hub...");
                if (acceptor != null) {
                    acceptor.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}