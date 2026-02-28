package com.yash.fixhub.core;

import java.time.LocalDateTime;

public class InternalOrder {

    private final String clOrdId;
    private final String symbol;
    private final Side side;
    private final double quantity;
    private final OrdType ordType;
    private final LocalDateTime transactTime;

    public InternalOrder(String clOrdId,
                         String symbol,
                         Side side,
                         double quantity,
                         OrdType ordType,
                         LocalDateTime transactTime) {

        this.clOrdId = clOrdId;
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.ordType = ordType;
        this.transactTime = transactTime;
    }

    public String getClOrdId() { return clOrdId; }
    public String getSymbol() { return symbol; }
    public Side getSide() { return side; }
    public double getQuantity() { return quantity; }
    public OrdType getOrdType() { return ordType; }
    public LocalDateTime getTransactTime() { return transactTime;
    
    
    
    }
    @Override
    public String toString() {
        return "InternalOrder{" +
                "clOrdId='" + clOrdId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", ordType=" + ordType +
                ", transactTime=" + transactTime +
                '}';
    }
}