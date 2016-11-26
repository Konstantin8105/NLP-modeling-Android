package com.modelingbrain.home.opensave.save;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelState;
import com.modelingbrain.home.opensave.SaveOpenActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

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
        Log.i(TAG, "prepare - start");

        ArrayList<Model> modelsDB = new ArrayList<>();
        {
            DBHelperModel dbHelperModel = new DBHelperModel(activity.getBaseContext());
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.NORMAL));
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.ARCHIVE));
            dbHelperModel.close();
        }

        Log.i(TAG, "SaveModel: IN");
        Log.i(TAG, "Saving models by JSON type");
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
            JSONObject obj = ModelToJson.convertModelToJson(model);
            if(obj != null){
                jsonArrayGlobal.put(obj);
            }
        }
        ModelToJson.saveJsonStringInFile(jsonArrayGlobal.toString());
        Log.i(TAG, "SaveModel: OUT");
    }


    private void publishProgress(int values) {
        if (values < 0)
            values = 0;
        if (values > 100)
            values = 100;
        activity.getProgressBar().setProgress(values);
    }
}