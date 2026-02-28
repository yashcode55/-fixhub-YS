package com.yash.fixhub.client;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yash.fixhub.core.FixLogUtil;

public class FixClientApplication implements Application {
	private static final Logger log =
	        LoggerFactory.getLogger(FixClientApplication.class);
    @Override
    public void onCreate(SessionID sessionId) {
        log.info("[CLIENT] Session created: " + sessionId);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        log.info("Client Logged On: " + sessionID);

        try {
            sendTestOrder(sessionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLogout(SessionID sessionId) {
    	log.info("[CLIENT] Logout: " + sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
    	log.info("[CLIENT] ToAdmin: " + FixLogUtil.pretty(message));
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    	log.info("[CLIENT] FromAdmin: " + FixLogUtil.pretty(message));
    }

    @Override
    public void toApp(Message message, SessionID sessionId)
            throws DoNotSend {
    	log.info("[CLIENT] ToApp: " + FixLogUtil.pretty(message));
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    	log.info("[CLIENT] FromApp: " + FixLogUtil.pretty(message));
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

        log.info("Sent NewOrderSingle from Client");
    }
}