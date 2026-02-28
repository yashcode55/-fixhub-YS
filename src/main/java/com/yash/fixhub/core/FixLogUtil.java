package com.yash.fixhub.core;

import quickfix.Message;

public class FixLogUtil {

    public static String pretty(Message message) {
        if (message == null) return "";
        return message.toString().replace('\001', '|');
    }
}