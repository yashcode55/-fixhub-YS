package com.yash.fixhub.application;

import com.yash.fixhub.core.FixLogUtil;
import com.yash.fixhub.core.MessageDispatcher;
import com.yash.fixhub.session.SessionContext;
import com.yash.fixhub.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.MsgType;

public class FixHubApplication implements Application {

    private static final Logger log =
            LoggerFactory.getLogger(FixHubApplication.class);

    private final SessionManager sessionManager;
    private final MessageDispatcher dispatcher;

    public FixHubApplication(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.dispatcher = new MessageDispatcher(sessionManager);
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("Session created: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {

        String counterparty = resolveCounterparty(sessionId);

        SessionContext context =
                sessionManager.getSession(counterparty);

        if (context == null) {
            log.error("SessionContext missing for {}", counterparty);
            return;
        }

        context.setSessionID(sessionId);

        log.info("Logon successful and bound for {}", counterparty);
    }

    @Override
    public void onLogout(SessionID sessionId) {

        String counterparty = resolveCounterparty(sessionId);

        SessionContext context =
                sessionManager.getSession(counterparty);

        if (context != null) {
            context.setSessionID(null);
        }

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

        if (MsgType.LOGON.equals(
                message.getHeader().getString(MsgType.FIELD))) {
            log.info("Received Logon request from {}", sessionId);
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionId)
            throws DoNotSend {
        log.debug("ToApp: {}", FixLogUtil.pretty(message));
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

        log.info("Incoming App Message from {}: {}",
                sessionId,
                FixLogUtil.pretty(message));

        try {
            dispatcher.dispatch(message, sessionId);
        } catch (Exception e) {
            log.error("Error processing message from session {}", sessionId, e);
        }
    }

    /**
     * Resolve actual counterparty compId dynamically
     */
    private String resolveCounterparty(SessionID sessionId) {

        if ("FIXHUB".equals(sessionId.getSenderCompID())) {
            return sessionId.getTargetCompID();
        } else {
            return sessionId.getSenderCompID();
        }
    }
}