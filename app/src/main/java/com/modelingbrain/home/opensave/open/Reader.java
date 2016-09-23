package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

import com.modelingbrain.home.opensave.SaveOpenActivity;
import com.modelingbrain.home.opensave.ValuesIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class Reader {
    @SuppressWarnings("unused")
    protected final String TAG = this.getClass().toString();

    protected JsonReader reader;
    private final AsyncTask<Void, String, Void> task;
    private final SaveOpenActivity activity;
//    throws IOException
    public Reader(AsyncTask<Void, String, Void> task, SaveOpenActivity activity) {
        this.activity = activity;
        this.task = task;
    }

    public void reading() throws IOException {
        init();
        openFile();
        readingFile();
    }

    private void openFile() throws FileNotFoundException {
        Log.d(TAG, "openFile - start");
        File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String full_path = sdPath.getAbsolutePath() + File.separator + ValuesIO.FILENAME;
        FileReader file = new FileReader(full_path);
        reader = new JsonReader(file);
        Log.d(TAG, "openFile - finish");
//        return;
    }

    private void readingFile() throws IOException {
//        Log.d(TAG, "readingFile - start");
        reader.beginArray();
        while (reader.hasNext()) {
//            Log.d(TAG, "readingFile - hasNext()");
            if(task.isCancelled()) {
                reader.close();
                return;
            }
            action();
            publishProgress(getPositionProgress());
        }
        reader.endArray();
        reader.close();
//        Log.d(TAG, "readingFile - finish");
    }

    public void close() throws IOException {
        reader.close();
    }

    abstract void init();
    abstract void action() throws IOException;
    abstract int getPositionProgress();
    private void publishProgress(int values){
        if(values < 0)
            values = 0;
        if(values > 100)
            values = 100;
        activity.getProgressBar().setProgress(values);
    }
}
