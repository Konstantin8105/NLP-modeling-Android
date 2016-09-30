package com.modelingbrain.home.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private static final SimpleDateFormat format = new SimpleDateFormat(
            "hh:mm:ss dd.MM.yyyy", Locale.getDefault());
    public static String ConvertMillisecondToDate(long date) {
        Date d = new Date(date);
        d.setTime(date);
        return format.format(d);
    }
}
