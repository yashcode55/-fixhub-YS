package com.yash.fixhub.client;

import quickfix.*;

public class FixClient {

public static void main(String[] args) throws Exception {

    SessionSettings settings = new SessionSettings("fixclient.cfg");

    Application application = new FixClientApplication();

    MessageStoreFactory storeFactory = new FileStoreFactory(settings);
    LogFactory logFactory = new FileLogFactory(settings);
    MessageFactory messageFactory = new DefaultMessageFactory();

    SocketInitiator initiator = new SocketInitiator(
            application,
            storeFactory,
            settings,
            logFactory,
            messageFactory
    );

    initiator.start();
    System.out.println("FIX Client started...");

    // Keep application alive
    Thread.sleep(Long.MAX_VALUE);
	}
}