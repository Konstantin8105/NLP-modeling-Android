package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.opensave.SaveOpenActivity;
import com.modelingbrain.home.opensave.ValuesIO;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ReaderAmount extends Reader {

    private int amount;

    public ReaderAmount(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, String filename, ValuesIO.formats format) {
        super(task, activity, filename, format);
    }

    @Override
    void init() {
        amount = 0;
    }

    @Override
    void action() throws IOException {
        amount++;
        switch (format) {
            case JSON:
                readerJson.skipValue();
                break;
            case XML:
                break;
            default:
                throw new FileNotFoundException("Cannot open that format: " + format);
        }
    }

    @Override
    int getPositionProgress() {
        return 0;
    }

    public int getAmount() {
        Log.i(TAG, "getAmount: amount = " + amount);
        return amount;
    }
}
