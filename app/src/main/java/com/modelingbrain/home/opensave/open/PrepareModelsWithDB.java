package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelState;
import com.modelingbrain.home.opensave.SaveOpenActivity;

import java.util.ArrayList;
import java.util.List;

public class PrepareModelsWithDB extends PrepareModels {
    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    public PrepareModelsWithDB(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, List<Model> models) {
        super(task, activity, models);
    }

    public void prepare() {
        Log.i(TAG, "prepare - start");

        ArrayList<Model> modelsDB = new ArrayList<>();
        {
            DBHelperModel dbHelperModel = new DBHelperModel(activity.getBaseContext());
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.NORMAL));
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.ARCHIVE));
            dbHelperModel.close();
        }
        ArrayList<CompareModel> compareModels = new ArrayList<>();
        for (Model modelDB : modelsDB) {
            CompareModel temp = new CompareModel(modelDB);
            temp.insideDB = true;
            compareModels.add(temp);
        }
        for (Model model : models) {
            compareModels.add(new CompareModel(model));
        }

        createListDeleteSame(compareModels);
        models = convertModels(compareModels);

        Log.i(TAG, "prepare - finish");
    }

}
