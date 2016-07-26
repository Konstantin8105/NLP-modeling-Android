package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.opensave.SaveOpenActivity;

import java.io.IOException;

public class ReaderAmount extends Reader{

    private int amount;

    public ReaderAmount(AsyncTask<Void, String, Void> task, SaveOpenActivity activity) throws IOException {
        super(task, activity);
    }

    @Override
    void init() {
        amount = 0;
    }

    @Override
    void action() throws IOException {
        amount ++;
        reader.skipValue();
    }

    @Override
    int getPositionProgress() {
        return 0;
    }

    public int getAmount(){
        Log.d(TAG, "getAmount: amount = "+amount);
        return amount;
    }
}
