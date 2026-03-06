package com.yash.fixhub.routing;

import com.yash.fixhub.order.OrderStateStore;
import com.yash.fixhub.session.SessionManager;
import com.yash.fixhub.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;

public class RoutingEngine {

    private static final Logger log =
            LoggerFactory.getLogger(RoutingEngine.class);

    private final SessionManager sessionManager;
    private final OrderStateStore orderStateStore;

    public RoutingEngine(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.orderStateStore = new OrderStateStore();
    }

    public void route(Message message, SessionID incomingSession)
            throws SessionNotFound, FieldNotFound {

        String msgType = message.getHeader().getString(MsgType.FIELD);

        /*
         * ==============================================
         * 1️⃣ BUY SIDE → SELL SIDE (New Order)
         * ==============================================
         */
        if (MsgType.ORDER_SINGLE.equals(msgType)) {

            String clOrdID = message.getString(ClOrdID.FIELD);

            String clientCompID = incomingSession.getTargetCompID();

            orderStateStore.registerOrder(clOrdID, clientCompID);

            SessionContext buyContext =
                    sessionManager.getSession(clientCompID);

            if (buyContext == null) {
                log.error("No SessionContext found for {}", clientCompID);
                return;
            }

            String targetBroker = buyContext.getDefaultRoute();

            if (targetBroker == null) {
                log.error("No defaultRoute configured for {}", clientCompID);
                return;
            }

            SessionContext sellContext =
                    sessionManager.getSession(targetBroker);

            if (sellContext == null || sellContext.getSessionID() == null) {
                log.error("Sell side {} not connected!", targetBroker);
                return;
            }

            log.info("Routing NewOrderSingle {} from {} to {}",
                    clOrdID, clientCompID, targetBroker);

            Session.sendToTarget(message, sellContext.getSessionID());
        }

        /*
         * ==============================================
         * 2️⃣ SELL SIDE → BUY SIDE (Execution Report)
         * ==============================================
         */
        else if (MsgType.EXECUTION_REPORT.equals(msgType)) {

            String clOrdID = message.getString(ClOrdID.FIELD);

            String targetClient =
                    orderStateStore.getClientForOrder(clOrdID);

            if (targetClient == null) {
                log.error("No client mapping found for order {}", clOrdID);
                return;
            }

            SessionContext buyContext =
                    sessionManager.getSession(targetClient);

            if (buyContext == null || buyContext.getSessionID() == null) {
                log.error("Buy side {} not connected!", targetClient);
                return;
            }

            log.info("Routing ExecutionReport for order {} back to {}",
                    clOrdID, targetClient);

            Session.sendToTarget(message, buyContext.getSessionID());

            char ordStatus = message.getChar(OrdStatus.FIELD);

            if (ordStatus == OrdStatus.FILLED) {
                orderStateStore.removeOrder(clOrdID);
                log.info("Order {} completed and removed from state store",
                        clOrdID);
            }
        }
    }
}