package com.modelingbrain.home.opensave;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.modelingbrain.home.R;
import com.modelingbrain.home.opensave.save.Writer;

public class SaveActivity extends SaveOpenActivity {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    @Override
    protected void initializeTask() {
        task = new SaveTask();
        task.execute();
    }

    private class SaveTask extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute - start");
            Log.d(TAG, "onPreExecute - finish");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground - start");
            try {
                publishProgress(getResources().getString(R.string.task_start_saving));
                Writer writer = new Writer(this,activity);
                writer.write();
                publishProgress(getResources().getString(R.string.task_file_created));
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
            finish();
            Log.d(TAG, "onPostExecute - finish");
        }
    }
}
