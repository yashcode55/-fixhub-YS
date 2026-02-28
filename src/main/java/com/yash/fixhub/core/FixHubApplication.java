package com.yash.fixhub.core;
import com.yash.fixhub.grammar.FixMessageConverter;
import com.yash.fixhub.core.InternalOrder;
import quickfix.field.MsgType;
import quickfix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixHubApplication implements Application {
	private static final Logger log =
	        LoggerFactory.getLogger(FixHubApplication.class);
    @Override
    public void onCreate(SessionID sessionId) {
        log.info("Session created: " + sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
    	log.info("Logon: " + sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
    	log.info("Logout: " + sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
    	log.info("ToAdmin: " + FixLogUtil.pretty(message));
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

    	log.info("SERVER FromAdmin: " + FixLogUtil.pretty(message));

        if (message.getHeader().getString(35).equals("A")) {
        	log.info("SERVER received Logon request");
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionId)
            throws DoNotSend {
    	log.info("ToApp: " + FixLogUtil.pretty(message));
    }

    @Override
    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

        String msgType = message.getHeader().getString(quickfix.field.MsgType.FIELD);

        // Detect NewOrderSingle
        if (quickfix.field.MsgType.ORDER_SINGLE.equals(msgType)) {

        	log.info("Received NewOrderSingle from session: " + sessionID);

            FixMessageConverter converter = new FixMessageConverter();

            try {
                InternalOrder internalOrder = converter.convertToInternalOrder(message);

                log.info("Converted Internal Order: " + internalOrder);

                // TODO: Pass to routing layer (next step)

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}