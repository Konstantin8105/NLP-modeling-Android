package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;
import android.util.Log;

import com.modelingbrain.home.GlobalFunction;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.SaveOpenActivity;

import java.util.ArrayList;

public class PrepareModels {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final String TAG = this.getClass().toString();

    protected ArrayList<Model> models;
    protected AsyncTask<Void, String, Void> task;
    protected SaveOpenActivity activity;

    public PrepareModels(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, ArrayList<Model> models) {
        Log.d(TAG, "PrepareModels - start");
        this.task = task;
        this.activity = activity;
        this.models = models;
        Log.d(TAG, "PrepareModels - finish");
    }

    protected class CompareModel {
        public CompareModel(Model model) {
            this.model = model;
            delete = false;
            insideDB = false;
        }

        public boolean delete;
        public Model model;
        public boolean insideDB;
    }

    protected void createListDeleteSame(ArrayList<CompareModel> compareModels) {
        for (int i = 0; i < compareModels.size(); i++) {
            if (task.isCancelled()) {
                return;
            }
            publishProgress((int) ((float) i / (float) compareModels.size() * 100));
            GlobalFunction.pause();
            for (int j = i + 1; j < compareModels.size(); j++) {
                if (i > compareModels.size() - 1) {
                    continue;
                }
                if (compareModels.get(i).model.compareTo(compareModels.get(j).model)) {
                    if (!compareModels.get(i).insideDB) {
                        compareModels.remove(i);
                        //continue;
                    } else if (!compareModels.get(j).insideDB) {
                        compareModels.remove(j);
                        j--;
                    } else if (compareModels.get(i).model.getMillisecond_Date() > compareModels.get(j).model.getMillisecond_Date()) {
                        compareModels.remove(j);
                        j--;
                    } else {
                        compareModels.remove(i);
                        //continue;
                    }
                }
            }
        }
        for (int i = compareModels.size() - 1; i >= 0; i--) {
            if (compareModels.get(i).insideDB)
                compareModels.remove(i);
        }


//        boolean[] list_delete = new boolean[compareModels.size()];
//        for(int i=0;i<list_delete.length;i++){
//            list_delete[i] = false;
//        }
//        for(int i=0;i<compareModels.size();i++)
//        {
//            if(task.isCancelled()) {
//                return;
//            }
//            GlobalFunction.pause();
//            if(!list_delete[i]) {
//                for (int j = i; j < compareModels.size(); j++) {
//                    if (j > i && !list_delete[j] && !list_delete[i]) {
//                        if (compareModels.get(i).model.compareTo(compareModels.get(j).model))
//                            if(!compareModels.get(i).insideDB){
//                                list_delete[i] = true;
//                                list_delete[j] = false;
//                                compareModels.get(i).delete = true;
//                                compareModels.get(j).delete = false;
//                            } else if(!compareModels.get(j).insideDB){
//                                list_delete[i] = false;
//                                list_delete[j] = true;
//                                compareModels.get(i).delete = false;
//                                compareModels.get(j).delete = true;
//                            } else {
//                                list_delete[i] = false;
//                                list_delete[j] = false;
//                                compareModels.get(i).delete = false;
//                                compareModels.get(j).delete = false;
//                            }
//                    }
//                }
//            }
//            publishProgress((int)((float)i/(float)models.size()*100));
//        }
    }

    protected ArrayList<Model> convertModels(ArrayList<CompareModel> compareModels) {
        ArrayList<Model> output = new ArrayList<>();
        for (int i = 0; i < compareModels.size(); i++) {
            if (!compareModels.get(i).delete && !compareModels.get(i).insideDB) {
                output.add(compareModels.get(i).model);
            }
        }
        return output;
    }

    protected void publishProgress(int values) {
        if (values < 0)
            values = 0;
        if (values > 100)
            values = 100;
        activity.getProgressBar().setProgress(values);
    }

    public ArrayList<Model> getModels() {
        return models;
    }

}
