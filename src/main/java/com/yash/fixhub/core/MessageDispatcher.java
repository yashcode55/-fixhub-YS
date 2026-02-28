package com.yash.fixhub.core;

import com.yash.fixhub.session.SessionRegistry;
import quickfix.Message;
import quickfix.SessionID;

public class MessageDispatcher {

    private final OrderService orderService;

    public MessageDispatcher(SessionRegistry sessionRegistry) {
        this.orderService = new OrderService(sessionRegistry);
    }

    public void dispatch(Message message, SessionID sessionID) throws Exception {

        String msgType = message.getHeader().getString(35);

        switch (msgType) {
            case "D":
                orderService.handleNewOrder(message, sessionID);
                break;

            default:
                break;
        }
    }
}