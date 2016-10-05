package com.modelingbrain.home.opensave;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private static final int REQUEST_READWRITE_STORAGE = 1000;

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    @Override
    protected void initializeTask() {
        task = new OpenTask();
        task.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READWRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void findAllFiles(List<String> fileList, String pattern) {
        Log.i(TAG, "findAllFiles - start");
        File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.i(TAG, "sdPath = " + sdPath + " :isExist = " + sdPath.exists() + " : isDirectory = " + sdPath.isDirectory());
        File[] files = sdPath.listFiles();
        if (files == null) {
            Log.i(TAG, "findAllFiles - finish - No files");
            return;
        }
        for (File file : files) {
            if (!file.isDirectory()) {
                if (file.getName().endsWith(pattern.toLowerCase())) {
                    Log.i(TAG, "found file " + file.getName());
                    fileList.add(file.getName());
                }
            }
        }
        Log.i(TAG, "findAllFiles - finish");
    }

    private class OpenTask extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute - start");
            Log.i(TAG, "onPreExecute - finish");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground - start");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_READWRITE_STORAGE);
                        // READ_EXTERNAL_STORAGE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
            }

            List<Model> models = new ArrayList<>();
            {
                for (int i = 0; i < ValuesIO.formats.values().length; i++) {
                    Log.i(TAG, "doInBackground - value : " + ValuesIO.formats.values()[i]);
                    List<String> files = new ArrayList<>();
                    findAllFiles(files, ValuesIO.formats.values()[i].getFormat());
                    for (int j = 0; j < files.size(); j++) {
                        Log.i(TAG, "doInBackground - file : " + files.get(j));
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

            //TODO remove same models in DB


            Log.i(TAG, "doInBackground - finish");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.i(TAG, "onProgressUpdate - start");
            createElement(values[0]);
            Log.i(TAG, "onProgressUpdate - finish");
        }

        @Override
        protected void onCancelled() {
            Log.i(TAG, "onCancelled - start");
            super.onCancelled();
            Log.i(TAG, "onCancelled - finish");
        }

        @Override
        protected void onCancelled(Void aVoid) {
            Log.i(TAG, "onCancelled(Void aVoid) - start");
            super.onCancelled(aVoid);
            Log.i(TAG, "onCancelled(Void aVoid) - finish");
        }

        @Override
        protected void onPostExecute(Void params) {
            Log.i(TAG, "onPostExecute - start");
            progressBar.setVisibility(View.GONE);
            Intent resultIntent = new Intent();
            setResult(MainActivity.RESULT_OK, resultIntent);
            finish();
            Log.i(TAG, "onPostExecute - finish");
        }
    }
}
