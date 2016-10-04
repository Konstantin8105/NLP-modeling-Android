package com.modelingbrain.home.opensave;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.modelingbrain.home.R;
import com.modelingbrain.home.opensave.save.Writer;

public class SaveActivity extends SaveOpenActivity {

    private static final int REQUEST_WRITE_STORAGE = 1000;

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    @Override
    protected void initializeTask() {
        task = new SaveTask();
        task.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
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

    private class SaveTask extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute - start");
            Log.d(TAG, "onPreExecute - finish");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground - start");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        // READ_EXTERNAL_STORAGE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
            }

            try {
                publishProgress(getResources().getString(R.string.task_start_saving));
                Writer writer = new Writer(this, activity);
                writer.write(getApplicationContext());
                publishProgress(getResources().getString(R.string.task_file_created));
            } catch (RuntimeException e) {
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
