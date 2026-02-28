package com.yash.fixhub.core;

import com.yash.fixhub.grammar.FixMessageConverter;
import com.yash.fixhub.session.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.MsgType;

public class FixHubApplication implements Application {

    private static final Logger log =
            LoggerFactory.getLogger(FixHubApplication.class);

    private final SessionRegistry sessionRegistry;
    private final MessageDispatcher dispatcher;

    public FixHubApplication(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
        this.dispatcher = new MessageDispatcher(sessionRegistry);
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("Session created: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        sessionRegistry.register(sessionId);
        log.info("Logon: {}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        sessionRegistry.deregister(sessionId);
        log.info("Logout: {}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.debug("ToAdmin: {}", FixLogUtil.pretty(message));
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound {

        log.debug("FromAdmin: {}", FixLogUtil.pretty(message));

        if (MsgType.LOGON.equals(message.getHeader().getString(MsgType.FIELD))) {
            log.info("Received Logon request from {}", sessionId);
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionId)
            throws DoNotSend {
        log.debug("ToApp: {}", FixLogUtil.pretty(message));
    }

    @Override
    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

        log.info("Incoming App Message from {}: {}",
                sessionID,
                FixLogUtil.pretty(message));

        try {
            dispatcher.dispatch(message, sessionID);
        } catch (Exception e) {
            log.error("Error processing message from session {}", sessionID, e);
        }
    }
}