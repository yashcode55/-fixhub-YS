package com.yash.fixhub.core;

import quickfix.*;

public class FixHubServer {

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
        System.out.println("FIX Hub started on port 9878...");

        // 🔥 Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Stopping FIX Hub...");
                if (acceptor != null) {
                    acceptor.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}