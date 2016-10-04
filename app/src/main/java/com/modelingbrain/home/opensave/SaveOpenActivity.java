package com.modelingbrain.home.opensave;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.modelingbrain.home.R;

abstract public class SaveOpenActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private LinearLayout linLayout;
    SaveOpenActivity activity;

    AsyncTask<Void, String, Void> task;

    ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate - start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_save);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        linLayout = (LinearLayout) findViewById(R.id.linear_layout);

        activity = this;
        initializeTask();

        Log.d(TAG, "onCreate - finish");
    }

    protected abstract void initializeTask();

    void createElement(String str) {
        Log.d(TAG, "createElement - start");
        Log.d(TAG, "createElement - str = " + str);
        if (str == null)
            str = "";//new String();
        LayoutInflater ltInflater = getLayoutInflater();
        View view = ltInflater.inflate(R.layout.one_row_opean_save, linLayout, false);
        TextView textView = (TextView) view.findViewById(R.id.textOneRow);
        textView.setText(str);
        linLayout.addView(view);
        Log.d(TAG, "createElement - finish");
    }

    @Override
    protected void onStop() {
        task.cancel(true);
        super.onStop();
    }

}
