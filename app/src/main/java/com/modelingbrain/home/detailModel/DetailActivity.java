package com.modelingbrain.home.detailModel;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;

public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    static public final String DATABASE_ID = "DATABASE_ID";
    static public final String STATE_DETAIL_ACTIVITY = "STATE_DETAIL_ACTIVITY";

    private Model model;

    private FloatingActionButton fab;
    private StageDetailActivity stageDetailActivity;
    private final String stageDetailActivityKey = "StageDetailActivity";
    private DetailFragments fragment;
    private final String detailFragmentsKey = "DetailFragments";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(detailFragmentsKey, fragment.toString());
        outState.putString(stageDetailActivityKey, stageDetailActivity.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate - start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int idDb = getIntent().getIntExtra(DATABASE_ID, 0);
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        model = dbHelperModel.openModel(idDb);

        initColors();

        stageDetailActivity = StageDetailActivity.valueOf(getIntent().getStringExtra(STATE_DETAIL_ACTIVITY));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStageDetail();
                createView();
                Snackbar.make(view, getBaseContext().getString(fragment.getStringResource()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d(TAG, "stageDetailActivity = " + stageDetailActivity.toString());
        if (stageDetailActivity == StageDetailActivity.STATE_READ_ONLY) {
            fab.hide();
        }

        if (savedInstanceState != null) {
            fragment = DetailFragments.valueOf(savedInstanceState.getString(detailFragmentsKey));
            stageDetailActivity = StageDetailActivity.valueOf(savedInstanceState.getString(stageDetailActivityKey));
        } else {
            switch (stageDetailActivity) {
                case STATE_NEW_FROM_WRITE:
                    fragment = DetailFragments.STATE_VIEW_WRITE;
                    break;
                default:
                    fragment = DetailFragments.STATE_VIEW_READ;
            }
        }

        createView();
        Log.d(TAG, "onCreate - finish");
    }

    private void initColors() {
        Log.d(TAG, "initColors - start");

        int generalModelColor = ContextCompat.getColor(getBaseContext(), model.getModelType().getGeneralColor());

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctl.setTitle(getResources().getStringArray(model.getModelID().getResourceQuestion())[0]);

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_detail_scroll);
        nestedScrollView.setBackgroundColor(generalModelColor);
        Log.d(TAG, "initColors - finish");
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