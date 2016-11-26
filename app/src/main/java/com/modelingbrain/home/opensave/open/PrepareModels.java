package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.SaveOpenActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class PrepareModels {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final String TAG = this.getClass().toString();

    List<Model> models;
    private final AsyncTask<Void, String, Void> task;
    final SaveOpenActivity activity;

    PrepareModels(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, List<Model> models) {
        Log.i(TAG, "PrepareModels - start");
        this.task = task;
        this.activity = activity;
        this.models = models;
        Log.i(TAG, "PrepareModels - finish");
    }

    static class CompareModel {
        public CompareModel(Model model) {
            this.model = model;
            delete = false;
            insideDB = false;
        }

        public final boolean delete;
        public final Model model;
        public boolean insideDB;
    }

    void createListDeleteSame(ArrayList<CompareModel> compareModels) {
        for (int i = 0; i < compareModels.size(); i++) {
            if (task.isCancelled()) {
                return;
            }
            if (i < 0)
                continue;
            publishProgress((int) ((float) i / (float) compareModels.size() * 100));
            GlobalFunction.pause();
            for (int j = i + 1; j < compareModels.size(); j++) {
                if (i > compareModels.size() - 1) {
                    continue;
                }
                if (compareModels.get(i).model.compareTo(compareModels.get(j).model)) {
                    if (!compareModels.get(i).insideDB) {
                        compareModels.remove(i);
                        j = compareModels.size();
                        i--;//continue;
                    } else if (!compareModels.get(j).insideDB) {
                        compareModels.remove(j);
                        j--;
                    } else if (compareModels.get(i).model.getMillisecond_Date() > compareModels.get(j).model.getMillisecond_Date()) {
                        compareModels.remove(j);
                        j--;
                    } else {
                        compareModels.remove(i);
                        j = compareModels.size();
                        i--;//continue;
                    }
                }
            }
        }
        for (int i = compareModels.size() - 1; i >= 0; i--) {
            if (compareModels.get(i).insideDB)
                compareModels.remove(i);
        }
    }

    List<Model> convertModels(List<CompareModel> compareModels) {
        ArrayList<Model> output = new ArrayList<>();
        for (CompareModel compareModel : compareModels) {
            if (!compareModel.delete && !compareModel.insideDB) {
                output.add(compareModel.model);
            }
        }
        return output;
    }

    private void publishProgress(int values) {
        if (values < 0)
            values = 0;
        if (values > 100)
            values = 100;
        activity.getProgressBar().setProgress(values);
    }

    public List<Model> getModels() {
        return models;
    }

}
