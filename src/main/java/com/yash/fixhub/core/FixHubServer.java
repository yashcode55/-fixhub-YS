package com.yash.fixhub.core;

import quickfix.*;

public class FixHubServer {

    public static void main(String[] args) throws Exception {

        SessionSettings settings = new SessionSettings("fixhub.cfg");

        Application application = new FixHubApplication();

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        SocketAcceptor acceptor = new SocketAcceptor(
                application,
                storeFactory,
                settings,
                logFactory,
                messageFactory
        );

        acceptor.start();
        System.out.println("FIX Hub started on port 9878...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                acceptor.stop();
                System.out.println("FIX Hub stopped.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}