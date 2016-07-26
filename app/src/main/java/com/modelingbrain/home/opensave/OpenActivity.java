package com.modelingbrain.home.opensave;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.modelingbrain.home.GlobalFunction;
import com.modelingbrain.home.MainActivity;
import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.open.PrepareModelsToDB;
import com.modelingbrain.home.opensave.open.PrepareModelsWithDB;
import com.modelingbrain.home.opensave.open.ReaderAmount;
import com.modelingbrain.home.opensave.open.ReaderModels;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
            try {
                publishProgress(getResources().getString(R.string.task_start_opening));
                ReaderAmount readerAmount = new ReaderAmount(this,activity);
                readerAmount.reading();
                int amountModels = readerAmount.getAmount();
                readerAmount.close();
                if(amountModels == 0)
                    return null;

                publishProgress(String.format(getResources().getString(R.string.task_amount_models), amountModels));
                publishProgress(getResources().getString(R.string.task_analyzing));
                ReaderModels readerModels = new ReaderModels(this,activity,amountModels);
                readerModels.reading();
                ArrayList<Model> models = readerModels.getModels();
                readerModels.close();
                if(models.size() == 0)
                    return null;
                publishProgress(getResources().getString(R.string.task_finish_analyze));

                publishProgress(getResources().getString(R.string.task_find_same_in_file));
                PrepareModelsToDB prepareModels = new PrepareModelsToDB(this,activity,models);
                prepareModels.prepare();
                models = prepareModels.getModels();
                publishProgress(String.format(getResources().getString(R.string.task_unique_models), models.size()));

                publishProgress(getResources().getString(R.string.task_find_same_in_db));
                PrepareModelsWithDB prepareWithModels = new PrepareModelsWithDB(this,activity,models);
                prepareWithModels.prepare();
                models = prepareWithModels.getModels();
                publishProgress(String.format(getResources().getString(R.string.task_amount_models_add),models.size()));

                GlobalFunction.pause();

                DBHelperModel dbHelperModel = new DBHelperModel(getBaseContext());
                dbHelperModel.addModelNormal(models);
                publishProgress(getResources().getString(R.string.task_added_in_db));

                GlobalFunction.pause();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e){
                e.printStackTrace();
            } catch (RuntimeException e){
                e.printStackTrace();
            }

            Log.d(TAG, "doInBackground - finish");
            return null;
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
