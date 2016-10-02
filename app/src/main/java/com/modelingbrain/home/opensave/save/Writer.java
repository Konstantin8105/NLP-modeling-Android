package com.modelingbrain.home.opensave.save;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.opensave.ValuesIO;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelState;
import com.modelingbrain.home.opensave.SaveOpenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {
    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private final AsyncTask<Void, String, Void> task;
    private final SaveOpenActivity activity;

    public Writer(AsyncTask<Void, String, Void> task, SaveOpenActivity activity) {
        this.task = task;
        this.activity = activity;
    }

    public void write(Context context) {
        Log.d(TAG, "prepare - start");

        ArrayList<Model> modelsDB = new ArrayList<>();
        {
            DBHelperModel dbHelperModel = new DBHelperModel(activity.getBaseContext());
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.NORMAL));
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.ARCHIVE));
            dbHelperModel.close();
        }

        Log.d(TAG, "SaveModel: IN");
        Log.d(TAG, "Saving models by JSON type");
        JSONArray jsonArrayGlobal = new JSONArray();
        for (int i = 0; i < modelsDB.size(); i++) {
            Model model = modelsDB.get(i);
            if (ContentManagerModel.isIgnore(context, model.getModelID()))
                continue;
            if (task.isCancelled()) {
                return;
            }
            GlobalFunction.pause();
            publishProgress((int) ((float) i / (float) modelsDB.size() * 100));
            try {
                JSONObject obj = new JSONObject();
                obj.put(ValuesIO.JsonElemenets.TYPE, model.getModelID().toString());
                obj.put(ValuesIO.JsonElemenets.NAME, model.getName());
                obj.put(ValuesIO.JsonElemenets.TIME, Long.valueOf(model.getMillisecond_Date()).toString());
                JSONArray list = new JSONArray();
                for (int nn = 0; nn < model.getModelID().getSize(); nn++)
                    list.put(model.getAnswer(nn));//.toString()
                obj.put(ValuesIO.JsonElemenets.RIGHT, list);
                jsonArrayGlobal.put(obj);
            } catch (final JSONException ignored) {
                ignored.printStackTrace();
            }
        }

        try {
            File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String full_path = sdPath.getAbsolutePath() + File.separator + ValuesIO.OUTPUT_FILENAME_JSON;
            boolean createFolder = sdPath.mkdirs();
            Log.d(TAG, "SaveModel: createFolder = " + createFolder);
            FileWriter file = new FileWriter(full_path);
            file.write(jsonArrayGlobal.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            Log.d(TAG, "SaveModel: ERROR WRITE FILE");
            e.printStackTrace();
        }
        Log.d(TAG, "SaveModel: OUT");
    }


    private void publishProgress(int values) {
        if (values < 0)
            values = 0;
        if (values > 100)
            values = 100;
        activity.getProgressBar().setProgress(values);
    }
}