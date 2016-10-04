package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

import com.modelingbrain.home.opensave.SaveOpenActivity;
import com.modelingbrain.home.opensave.ValuesIO;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class Reader {
    @SuppressWarnings("unused")
    protected final String TAG = this.getClass().toString();

    JsonReader readerJson;
    private BufferedReader xmlBuffer;
    XmlPullParser xpp;
    private final AsyncTask<Void, String, Void> task;
    private final SaveOpenActivity activity;
    private final String filename;
    final ValuesIO.formats format;

    Reader(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, String filename, ValuesIO.formats format) {
        this.activity = activity;
        this.task = task;
        this.filename = filename;
        this.format = format;
    }

    public void reading() throws IOException, XmlPullParserException {
        init();
        openFile();
        readingFile();
    }

    private void openFile() throws FileNotFoundException {
        Log.d(TAG, "openFile - start");
        File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String full_path = sdPath.getAbsolutePath() + File.separator + filename;
        switch (format) {
            case JSON:
                FileReader file = new FileReader(full_path);
                readerJson = new JsonReader(file);
                break;
            case XML:
                xmlBuffer = new BufferedReader(new FileReader(full_path));
                break;
            default:
                throw new FileNotFoundException("Cannot open that format: " + format);
        }
        Log.d(TAG, "openFile - finish");
    }

    private void readingFile() throws IOException, XmlPullParserException {
        switch (format) {
            case JSON: {
                readerJson.beginArray();
                while (readerJson.hasNext()) {
                    if (task.isCancelled()) {
                        close();
                        return;
                    }
                    action();
                    publishProgress(getPositionProgress());
                }
                readerJson.endArray();
                break;
            }
            case XML: {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                xpp = factory.newPullParser();
                xpp.setInput(xmlBuffer);
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                    } else if (xpp.getAttributeCount() > 0) {
                        if (task.isCancelled()) {
                            close();
                            return;
                        }
                        action();
                        publishProgress(getPositionProgress());
                    }
                    eventType = xpp.next();
                }
                break;
            }
            default:
                throw new FileNotFoundException("Cannot open that format: " + format);
        }
        close();
    }

    public void close() throws IOException {
        switch (format) {
            case JSON:
                readerJson.close();
                break;
            case XML:
                xmlBuffer.close();
                break;
            default:
                throw new FileNotFoundException("Cannot open that format: " + format);
        }
    }

    abstract void init();

    abstract void action() throws IOException;

    abstract int getPositionProgress();

    private void publishProgress(int values) {
        if (values < 0)
            values = 0;
        if (values > 100)
            values = 100;
        activity.getProgressBar().setProgress(values);
    }
}
