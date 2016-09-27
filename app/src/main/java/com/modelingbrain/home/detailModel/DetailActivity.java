package com.modelingbrain.home.detailModel;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;

public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    static public final String DATABASE_ID = "DATABASE_ID";
    static public final String STATE_DETAIL_ACTIVITY = "STATE_DETAIL_ACTIVITY";

    private Model model;
    private final String modelKey = "Model";

    private FloatingActionButton fab;
    private StageDetailActivity stageDetailActivity;
    private final String stageDetailActivityKey = "StageDetailActivity";
    private DetailFragments fragment;
    private final String detailFragmentsKey = "DetailFragments";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        dbHelperModel.updateModel(model);

        outState.putString(detailFragmentsKey, fragment.toString());
        outState.putString(stageDetailActivityKey, stageDetailActivity.toString());
        outState.putInt(modelKey, model.getDbId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate - start");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStageDetail();
                createView();
                Snackbar.make(findViewById(R.id.header), getBaseContext().getString(fragment.getStringResource()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        int idDb;

        if (savedInstanceState != null) {
            idDb = savedInstanceState.getInt(modelKey);
            stageDetailActivity = StageDetailActivity.valueOf(savedInstanceState.getString(stageDetailActivityKey));
            fragment = DetailFragments.valueOf(savedInstanceState.getString(detailFragmentsKey));
        } else {
            idDb = getIntent().getIntExtra(DATABASE_ID, 0);
            stageDetailActivity = StageDetailActivity.valueOf(getIntent().getStringExtra(STATE_DETAIL_ACTIVITY));
            switch (stageDetailActivity) {
                case STATE_NEW_FROM_WRITE:
                    fragment = DetailFragments.STATE_VIEW_WRITE;
                    break;
                default:
                    fragment = DetailFragments.STATE_VIEW_READ;
            }
        }
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        model = dbHelperModel.openModel(idDb);

        if (stageDetailActivity == StageDetailActivity.STATE_READ_ONLY) {
            fab.hide();
        }

        TextView modelName = (TextView) findViewById(R.id.model_name);
        modelName.setText(getResources().getTextArray(model.getModelID().getResourceQuestion())[0]);

        ScrollView header = (ScrollView) findViewById(R.id.header);
        header.setBackgroundColor(ContextCompat.getColor(getBaseContext(), model.getModelType().getGeneralColor()));

        ImageView icon = (ImageView) findViewById(R.id.icon);
        icon.setImageResource(model.getModelID().getResourceIcon());

        createView();
        Log.d(TAG, "onCreate - finish");
    }

    private void changeStageDetail() {
        if (stageDetailActivity == StageDetailActivity.STATE_READ_ONLY)
            return;
        switch (fragment) {
            case STATE_VIEW_READ:
                fragment = DetailFragments.STATE_VIEW_WRITE;
                break;
            case STATE_VIEW_WRITE:
                fragment = DetailFragments.STATE_VIEW_READ;
                break;
            default:
                throw new RuntimeException("Add new view");
        }
    }


    private void createView() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (fragment) {
            case STATE_VIEW_READ: {
                transaction.remove(fragment.getFragment());
                fragment.getFragment().send(model);
                transaction.replace(R.id.detail_fragment, fragment.getFragment());
                break;
            }
            case STATE_VIEW_WRITE: {
                transaction.remove(fragment.getFragment());
                fragment.getFragment().send(model);
                transaction.replace(R.id.detail_fragment, fragment.getFragment());
                break;
            }
            default:
                throw new RuntimeException("Add new view");
        }
        transaction.commit();

        fab.setImageResource(fragment.getFabIconResource());
    }

    @Override
    protected void onPause() {
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        dbHelperModel.updateModel(model);
        setResult(RESULT_OK);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed - start");
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        dbHelperModel.updateModel(model);
        setResult(RESULT_OK);
        super.onBackPressed();
        Log.d(TAG, "onBackPressed - finish");
    }
}