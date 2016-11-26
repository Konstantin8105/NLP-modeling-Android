package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.SaveOpenActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class PrepareModelsToDB extends PrepareModels {
    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    public PrepareModelsToDB(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, List<Model> models) {
        super(task, activity, models);
    }

    public void prepare() {
        Log.i(TAG, "prepare - start");

        ArrayList<CompareModel> compareModels = new ArrayList<>();
        for (Model model : models) {
            compareModels.add(new CompareModel(model));
        }
        createListDeleteSame(compareModels);
        models = convertModels(compareModels);
        Log.i(TAG, "prepare - finish");
    }
}
