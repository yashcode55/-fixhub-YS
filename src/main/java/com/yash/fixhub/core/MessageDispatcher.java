package com.yash.fixhub.core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Message;
import quickfix.SessionID;

public class MessageDispatcher {
	private static final Logger log =
	        LoggerFactory.getLogger(MessageDispatcher.class);
    private final OrderService orderService;

    public MessageDispatcher() {
        this.orderService = new OrderService();
    }

    public void dispatch(Message message, SessionID sessionID) throws Exception {

        String msgType = message.getHeader().getString(35);

        switch (msgType) {
            case "D":
                orderService.handleNewOrder(message, sessionID);
                break;

            default:
                log.info("Unsupported message type: " + msgType);
        }
    }
}