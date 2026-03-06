package com.yash.fixhub.broker;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FixVersions;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

import quickfix.field.*;

import java.util.Date;

public class FixBrokerApplication extends MessageCracker implements Application {

    @Override
    public void onCreate(SessionID sessionId) {}

    @Override
    public void onLogon(SessionID sessionId) {
        System.out.println("[BROKER] Logon: " + sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        System.out.println("[BROKER] Logout: " + sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {}

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat,
                   IncorrectTagValue, RejectLogon {}

    @Override
    public void toApp(Message message, SessionID sessionId)
            throws DoNotSend {}

    @Override
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat,
                   IncorrectTagValue, UnsupportedMessageType {

        String msgType = message.getHeader().getString(MsgType.FIELD);

        if (MsgType.ORDER_SINGLE.equals(msgType)) {
            handleNewOrder(message, sessionId);
        } else {
            throw new UnsupportedMessageType();
        }
    }

    private void handleNewOrder(Message message, SessionID sessionId)
            throws FieldNotFound {

        String clOrdID = message.getString(ClOrdID.FIELD);
        double qty = message.getDouble(OrderQty.FIELD);
        char side = message.getChar(Side.FIELD);
        String symbol = message.getString(Symbol.FIELD);

        System.out.println("[BROKER] Received Order: " + clOrdID);

        // Send NEW execution
        sendExecutionReport(sessionId, clOrdID, symbol, side, qty,
                ExecType.NEW, OrdStatus.NEW);

        // Send FILLED execution
        sendExecutionReport(sessionId, clOrdID, symbol, side, qty,
                ExecType.FILL, OrdStatus.FILLED);
    }

    private void sendExecutionReport(SessionID sessionId,
                                     String clOrdID,
                                     String symbol,
                                     char side,
                                     double qty,
                                     char execType,
                                     char ordStatus) {

        try {

            String beginString = sessionId.getBeginString();

            double leavesQty = (ordStatus == OrdStatus.FILLED) ? 0 : qty;
            double cumQty = (ordStatus == OrdStatus.FILLED) ? qty : 0;

            Message executionReport;

            if (FixVersions.BEGINSTRING_FIX42.equals(beginString)) {

                executionReport =
                        new quickfix.fix42.ExecutionReport(
                                new OrderID("ORD-" + System.currentTimeMillis()),
                                new ExecID("EXEC-" + System.nanoTime()),
                                new ExecTransType(ExecTransType.NEW),
                                new ExecType(execType),
                                new OrdStatus(ordStatus),
                                new Symbol(symbol),
                                new Side(side),
                                new LeavesQty(leavesQty),
                                new CumQty(cumQty),
                                new AvgPx(100.00)
                        );

            } else if (FixVersions.BEGINSTRING_FIX44.equals(beginString)) {

                executionReport =
                        new quickfix.fix44.ExecutionReport(
                                new OrderID("ORD-" + System.currentTimeMillis()),
                                new ExecID("EXEC-" + System.nanoTime()),
                                new ExecType(execType),
                                new OrdStatus(ordStatus),
                                new Side(side),
                                new LeavesQty(leavesQty),
                                new CumQty(cumQty),
                                new AvgPx(100.00)
                        );

                executionReport.setField(new Symbol(symbol));

            } else {
                throw new RuntimeException("Unsupported FIX version: " + beginString);
            }

            executionReport.setField(new ClOrdID(clOrdID));
            executionReport.setField(new TransactTime());

            Session.sendToTarget(executionReport, sessionId);

            System.out.println("[BROKER] Sent ExecutionReport: " + ordStatus);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}