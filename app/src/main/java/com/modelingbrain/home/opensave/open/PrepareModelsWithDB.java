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
        Log.d(TAG, "prepare - start");

        ArrayList<Model> modelsDB = new ArrayList<>();
        {
            DBHelperModel dbHelperModel = new DBHelperModel(activity.getBaseContext());
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.NORMAL));
            modelsDB.addAll(dbHelperModel.openHeader(ModelState.ARCHIVE));
            dbHelperModel.close();
        }
        ArrayList<CompareModel> compareModels = new ArrayList<>();
        for (int i = 0; i < modelsDB.size(); i++) {
            CompareModel temp = new CompareModel(modelsDB.get(i));
            temp.insideDB = true;
            compareModels.add(temp);
        }
        for (int i = 0; i < models.size(); i++) {
            compareModels.add(new CompareModel(models.get(i)));
        }

        createListDeleteSame(compareModels);
        models = convertModels(compareModels);

        Log.d(TAG, "prepare - finish");
    }

}
