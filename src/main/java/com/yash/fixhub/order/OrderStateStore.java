package com.yash.fixhub.order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderStateStore {

    // ClOrdID -> Client CompID
    private final Map<String, String> orderToClientMap =
            new ConcurrentHashMap<>();

    public void registerOrder(String clOrdID, String clientCompID) {
        orderToClientMap.put(clOrdID, clientCompID);
    }

    public String getClientForOrder(String clOrdID) {
        return orderToClientMap.get(clOrdID);
    }

    public void removeOrder(String clOrdID) {
        orderToClientMap.remove(clOrdID);
    }
}