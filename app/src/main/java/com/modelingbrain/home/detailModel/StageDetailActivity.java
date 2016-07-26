package com.modelingbrain.home.detailModel;

import android.util.Log;

public enum StageDetailActivity {
    STATE_NORMAL_FROM_READ,
    STATE_NEW_FROM_WRITE,
    STATE_READ_ONLY;

    private static final String TAG = "myLogs";
    static public StageDetailActivity Convert(String string){
        Log.d(TAG, "StageDetailActivity input = "+string);
        StageDetailActivity stage = STATE_READ_ONLY;
        if(string.compareTo(STATE_NORMAL_FROM_READ.toString())==0)
            stage = STATE_NORMAL_FROM_READ;
        else if(string.compareTo(STATE_NEW_FROM_WRITE.toString())==0)
            stage = STATE_NEW_FROM_WRITE;
        else if(string.compareTo(STATE_READ_ONLY.toString())==0)
            stage = STATE_READ_ONLY;
        Log.d(TAG, "StageDetailActivity output = "+stage);
        return stage;
    }
}