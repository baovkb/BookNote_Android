package com.vkbao.notebook.helper;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConvertor {
    public static long getCurrentUnixSecond() {
        return (new Date()).getTime()/1000;
    }

    public static long getCurrentUnixMiliSecond() {
        return (new Date()).getTime();
    }

    public static String convertUnixToLocalTime(long unixSecond) {
        return (new SimpleDateFormat("HH:mm:ss dd-MM-yyy")).format(unixSecond*1000);
    }

    public static String convertUnixToLocalTime(long unixSecond, String formatString) {
        return (new SimpleDateFormat(formatString)).format(unixSecond*1000);
    }
}
