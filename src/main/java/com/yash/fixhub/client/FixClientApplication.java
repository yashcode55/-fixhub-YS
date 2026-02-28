package com.yash.fixhub.client;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.*;

public class FixClientApplication implements Application {

    @Override
    public void onCreate(SessionID sessionId) {
        System.out.println("[CLIENT] Session created: " + sessionId);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        System.out.println("Client Logged On: " + sessionID);

        try {
            sendTestOrder(sessionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    public void sendTestOrder(SessionID sessionID) throws Exception {

        quickfix.fix44.NewOrderSingle order =
                new quickfix.fix44.NewOrderSingle(
                        new quickfix.field.ClOrdID("ORDER-1"),
                        new quickfix.field.Side(quickfix.field.Side.BUY),
                        new quickfix.field.TransactTime(),
                        new quickfix.field.OrdType(quickfix.field.OrdType.MARKET)
                );
        order.set(new quickfix.field.HandlInst('1'));
        order.set(new quickfix.field.Symbol("AAPL"));
        order.set(new quickfix.field.OrderQty(100));

        quickfix.Session.sendToTarget(order, sessionID);

        System.out.println("Sent NewOrderSingle from Client");
    }
}