package com.transAI.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateLogger {
    public static String getTime() {
        // return present moment in hh:mm:ss format
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
}
