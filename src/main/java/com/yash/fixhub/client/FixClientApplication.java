package com.yash.fixhub.client;

import quickfix.*;

public class FixClientApplication implements Application {

    @Override
    public void onCreate(SessionID sessionId) {
        System.out.println("[CLIENT] Session created: " + sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        System.out.println("[CLIENT] Logon successful: " + sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        System.out.println("[CLIENT] Logout: " + sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        System.out.println("[CLIENT] ToAdmin: " + message);
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        System.out.println("[CLIENT] FromAdmin: " + message);
    }

    @Override
    public void toApp(Message message, SessionID sessionId)
            throws DoNotSend {
        System.out.println("[CLIENT] ToApp: " + message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        System.out.println("[CLIENT] FromApp: " + message);
    }
}