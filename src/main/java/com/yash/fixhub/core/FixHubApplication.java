package com.yash.fixhub.core;

import quickfix.*;

public class FixHubApplication implements Application {

    @Override
    public void onCreate(SessionID sessionId) {
        System.out.println("Session created: " + sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        System.out.println("Logon: " + sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        System.out.println("Logout: " + sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        System.out.println("ToAdmin: " + message);
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

        System.out.println("SERVER FromAdmin: " + message);

        if (message.getHeader().getString(35).equals("A")) {
            System.out.println("SERVER received Logon request");
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionId)
            throws DoNotSend {
        System.out.println("ToApp: " + message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        System.out.println("FromApp: " + message);
    }
}