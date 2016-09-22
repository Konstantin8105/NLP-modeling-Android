package com.modelingbrain.home;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalFunction {
    public static void pause() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long getTime() {
        return (new Date()).getTime();
    }

    public static String ConvertMillisecondToDate(long date) {
        Date d = new Date(date);
        d.setTime(date);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss dd.MM.yyyy");
        return format.format(d);
    }
}
