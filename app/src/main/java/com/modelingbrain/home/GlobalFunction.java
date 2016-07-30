package com.modelingbrain.home;

import java.util.Calendar;
import java.util.Date;

public class GlobalFunction {
    // TODO: 7/30/16 remove strange functions in comments
    private static final String TAG = "myLogs";

//    public static int random(int value){
//        float random = (float) (Math.random()*value);
//        return (int) random;
//    }

    public static void pause() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*public static String ConvertMillisecondToTime(long time)
    {
        String str = new String();
        if(time>24*3600*1000)
            str = "NONE";
        else
        {
            int hour = (int)(long)(time/(3600*1000));
            int min  = (int)(long)((time-hour*3600*1000)/(60*1000));
            int sec  = (int)(long)((time-hour*3600*1000-min*60*1000)/(1000));
            str = hour+":"+min+":"+sec;
        }
        return str;
    }*/

    public static long getTime()
    {
        return (new Date()).getTime();
    }

    public static String ConvertMillisecondToDate(long date)
    {
//        Log.d(TAG, "GlobalFunction :: ConvertMillisecondToDate - start = "+date);
        Date d = new Date(date);
        String str;// = new String();
        Calendar cc = Calendar.getInstance();
        cc.setTime(d);

        str = "";
        int hour = cc.get(Calendar.HOUR);
        if(cc.get(Calendar.AM_PM) != 0)
        {
            hour += 12;
        }
        if(hour<10)
            str += "0";
        str += hour+"";

        str += ":";

        if(cc.get(Calendar.MINUTE)<10)
            str += "0";
        str += cc.get(Calendar.MINUTE);

        str += "  ";

        if(cc.get(Calendar.DAY_OF_MONTH)<10)
            str += "0";
        str += cc.get(Calendar.DAY_OF_MONTH);

        str += "/";

        if((cc.get(Calendar.MONTH)+1)<10)
            str += "0";
        str += (cc.get(Calendar.MONTH)+1)+"";

        str += "/";

        str += cc.get(Calendar.YEAR);

//        Log.d(TAG, "GlobalFunction :: ConvertMillisecondToDate - end = "+str);
        return str;
    }

//    public static long ConvertToSecond(long l) {
//        return l/1000l;
//    }
}
