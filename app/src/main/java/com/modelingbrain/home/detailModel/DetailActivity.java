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
import com.modelingbrain.home.detailModel.fragments.StageFragment;
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

    private FragmentType fragmentType;


    private final String detailFragmentsKey = "FragmentType";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        savingModelInDb();

        outState.putString(detailFragmentsKey, fragmentType.toString());
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
                Snackbar.make(findViewById(R.id.header), getBaseContext().getString(fragmentType.getStringResource()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        int idDb;

        if (savedInstanceState != null) {
            idDb = savedInstanceState.getInt(modelKey);
            stageDetailActivity = StageDetailActivity.valueOf(savedInstanceState.getString(stageDetailActivityKey));
            fragmentType = FragmentType.valueOf(savedInstanceState.getString(detailFragmentsKey));
        } else {
            idDb = getIntent().getIntExtra(DATABASE_ID, 0);
            stageDetailActivity = StageDetailActivity.valueOf(getIntent().getStringExtra(STATE_DETAIL_ACTIVITY));
            switch (stageDetailActivity) {
                case STATE_NEW_FROM_WRITE:
                    fragmentType = FragmentType.STATE_VIEW_WRITE;
                    break;
                default:
                    fragmentType = FragmentType.STATE_VIEW_READ;
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
        switch (fragmentType) {
            case STATE_VIEW_READ:
                fragmentType = FragmentType.STATE_VIEW_WRITE;
                break;
            case STATE_VIEW_WRITE:
                fragmentType = FragmentType.STATE_VIEW_READ;
                break;
            default:
                throw new RuntimeException("Add new view");
        }
    }


    private void createView() {



        StageFragment stageFragment = fragmentType.getNewInstanceFragment(getFragmentManager().beginTransaction());

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putInt(DATABASE_ID,model.getDbId());
        stageFragment.setArguments(args);
//        fragmentType.getFragment().send(model);
        switch (fragmentType) {
            case STATE_VIEW_READ: {
//                transaction.remove(fragmentType.getFragment());
                transaction.add(R.id.detail_fragment, stageFragment);
                break;
            }
            case STATE_VIEW_WRITE: {
//                transaction.remove(fragmentType.getFragment());
                transaction.add(R.id.detail_fragment, stageFragment);
                break;
            }
            default:
                throw new RuntimeException("Add new view");
        }
//        transaction.addToBackStack(null);
        transaction.commit();

        fab.setImageResource(fragmentType.getFabIconResource());
    }

    @Override
    protected void onPause() {
        savingModelInDb();
        setResult(RESULT_OK);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed - start");
        savingModelInDb();
        setResult(RESULT_OK);
        super.onBackPressed();
        Log.d(TAG, "onBackPressed - finish");
    }

    private void savingModelInDb(){
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        dbHelperModel.updateModel(model);
        dbHelperModel.close();
    }
}