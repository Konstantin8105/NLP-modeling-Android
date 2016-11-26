package com.modelingbrain.home.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class GlobalFunction {
    public static void pause() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long getTime() {
        return (new Date()).getTime();
    }

    public static String convertMillisecondToDate(long date) {
        Date d = new Date(date);
        d.setTime(date);
        SimpleDateFormat format = new SimpleDateFormat(
                "hh:mm:ss dd.MM.yyyy", Locale.getDefault());
        return format.format(d);
    }
}
