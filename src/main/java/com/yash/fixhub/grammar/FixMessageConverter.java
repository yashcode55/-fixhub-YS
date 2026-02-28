package com.yash.fixhub.grammar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yash.fixhub.core.InternalOrder;
import com.yash.fixhub.core.Side;
import com.yash.fixhub.core.OrdType;

import quickfix.FieldNotFound;
import quickfix.Message;

import quickfix.field.ClOrdID;
import quickfix.field.Symbol;
import quickfix.field.OrderQty;
import quickfix.field.TransactTime;

import java.time.LocalDateTime;

public class FixMessageConverter {
	private static final Logger log =
	        LoggerFactory.getLogger(FixMessageConverter.class);
    public InternalOrder convertToInternalOrder(Message message) throws FieldNotFound {

        String clOrdId = message.getString(ClOrdID.FIELD);
        String symbol = message.getString(Symbol.FIELD);
        double quantity = message.getDouble(OrderQty.FIELD);

        char fixSide = message.getChar(quickfix.field.Side.FIELD);
        Side side = mapSide(fixSide);

        char fixOrdType = message.getChar(quickfix.field.OrdType.FIELD);
        OrdType ordType = mapOrdType(fixOrdType);

        // ✅ Directly get LocalDateTime
        LocalDateTime transactTime = message.getUtcTimeStamp(TransactTime.FIELD);

        return new InternalOrder(
                clOrdId,
                symbol,
                side,
                quantity,
                ordType,
                transactTime
        );
    }

    private Side mapSide(char fixSide) {
        switch (fixSide) {
            case '1': return Side.BUY;
            case '2': return Side.SELL;
            default: throw new IllegalArgumentException("Unsupported FIX Side: " + fixSide);
        }
    }

    private OrdType mapOrdType(char fixOrdType) {
        switch (fixOrdType) {
            case '1': return OrdType.MARKET;
            case '2': return OrdType.LIMIT;
            default: throw new IllegalArgumentException("Unsupported FIX OrdType: " + fixOrdType);
        }
    }
}