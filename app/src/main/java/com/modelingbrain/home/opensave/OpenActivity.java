package com.modelingbrain.home.opensave;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.MainActivity;
import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.open.PrepareModelsToDB;
import com.modelingbrain.home.opensave.open.PrepareModelsWithDB;
import com.modelingbrain.home.opensave.open.ReaderAmount;
import com.modelingbrain.home.opensave.open.ReaderModels;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenActivity extends SaveOpenActivity {
    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    @Override
    protected void initializeTask() {
        task = new OpenTask();
        task.execute();
    }

    private class OpenTask extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute - start");
            Log.d(TAG, "onPreExecute - finish");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground - start");


            List<Model> models = new ArrayList<>();
            {
                for (int i = 0; i < ValuesIO.formats.values().length; i++) {
                    Log.d(TAG, "doInBackground - value : " + ValuesIO.formats.values()[i]);
                    List<String> files = new ArrayList<>();
                    findAllFiles(files, ValuesIO.formats.values()[i].getFormat());
                    for (int j = 0; j < files.size(); j++) {
                        Log.d(TAG, "doInBackground - file : " + files.get(j));
                        try {
                            publishProgress(getResources().getString(R.string.task_filename) + files.get(j));
                            publishProgress(getResources().getString(R.string.task_start_opening));
                            ReaderAmount readerAmount = new ReaderAmount(this, activity, files.get(j), ValuesIO.formats.values()[i]);
                            readerAmount.reading();
                            int amountModels = readerAmount.getAmount();
                            readerAmount.close();
                            if (amountModels == 0)
                                continue;

                            publishProgress(getResources().getQuantityString(R.plurals.task_amount_models, amountModels, amountModels));
                            publishProgress(getResources().getString(R.string.task_analyzing));
                            ReaderModels readerModels = new ReaderModels(this, activity, files.get(j), amountModels, ValuesIO.formats.values()[i]);
                            readerModels.reading();
                            List<Model> modelReader = readerModels.getModels();
                            readerModels.close();
                            if (modelReader == null)
                                continue;
                            if (modelReader.size() > 0)
                                models.addAll(modelReader);
                            publishProgress(getResources().getString(R.string.task_finish_analyze));

                        } catch (IOException | RuntimeException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (models.size() == 0)
                return null;

            publishProgress(getResources().getString(R.string.task_find_same_in_file));
            PrepareModelsToDB prepareModels = new PrepareModelsToDB(this, activity, models);
            prepareModels.prepare();
            models = prepareModels.getModels();
            publishProgress(getResources().getQuantityString(R.plurals.task_unique_models,
                    models.size(), models.size()));

            publishProgress(getResources().getString(R.string.task_find_same_in_db));
            PrepareModelsWithDB prepareWithModels = new PrepareModelsWithDB(this, activity, models);
            prepareWithModels.prepare();
            models = prepareWithModels.getModels();
            publishProgress(getResources().getQuantityString(R.plurals.task_amount_models_add,
                    models.size(), models.size()));

            GlobalFunction.pause();

            DBHelperModel dbHelperModel = new DBHelperModel(getBaseContext());
            dbHelperModel.addModelNormal(models);
            publishProgress(getResources().getString(R.string.task_added_in_db));

            GlobalFunction.pause();


            Log.d(TAG, "doInBackground - finish");
            return null;
        }

        private void findAllFiles(List<String> fileList, String pattern) {
            Log.d(TAG, "findAllFiles - start");
            File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Log.d(TAG, "sdPath = " + sdPath + " :isExist = " + sdPath.exists() + " : isDirectory = " + sdPath.isDirectory());
            File[] files = sdPath.listFiles();
            if (files == null) {
                Log.d(TAG, "findAllFiles - finish - No files");
                return;
            }
            for (File file : files) {
                if (!file.isDirectory()) {
                    if (file.getName().endsWith(pattern.toLowerCase())) {
                        Log.d(TAG, "found file " + file.getName());
                        fileList.add(file.getName());
                    }
                }
            }
            Log.d(TAG, "findAllFiles - finish");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.d(TAG, "onProgressUpdate - start");
            createElement(values[0]);
            Log.d(TAG, "onProgressUpdate - finish");
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled - start");
            super.onCancelled();
            Log.d(TAG, "onCancelled - finish");
        }

        @Override
        protected void onCancelled(Void aVoid) {
            Log.d(TAG, "onCancelled(Void aVoid) - start");
            super.onCancelled(aVoid);
            Log.d(TAG, "onCancelled(Void aVoid) - finish");
        }

        @Override
        protected void onPostExecute(Void params) {
            Log.d(TAG, "onPostExecute - start");
            progressBar.setVisibility(View.GONE);
            Intent resultIntent = new Intent();
            setResult(MainActivity.RESULT_OK, resultIntent);
            finish();
            Log.d(TAG, "onPostExecute - finish");
        }
    }
}
