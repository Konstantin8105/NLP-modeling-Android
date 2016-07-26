package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.SaveOpenActivity;

import java.util.ArrayList;

public class PrepareModelsToDB extends PrepareModels{
    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    public PrepareModelsToDB(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, ArrayList<Model> models) {
        super(task, activity, models);
    }

    public void prepare() {
        Log.d(TAG, "prepare - start");

        ArrayList<CompareModel> compareModels = new ArrayList<>();
        for(int i=0;i<models.size();i++){
            compareModels.add(new CompareModel(models.get(i)));
        }
        createListDeleteSame(compareModels);
        models = convertModels(compareModels);
        Log.d(TAG, "prepare - finish");
    }
}
