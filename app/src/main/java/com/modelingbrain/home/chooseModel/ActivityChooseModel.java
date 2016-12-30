package com.modelingbrain.home.chooseModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.detailModel.DetailActivity;
import com.modelingbrain.home.detailModel.StageDetailActivity;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;
import com.modelingbrain.home.template.AdapterTemplate;
import com.modelingbrain.home.template.ElementList;

import java.util.ArrayList;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class ActivityChooseModel extends AppCompatActivity implements
        AdapterTemplate.ViewHolder.ClickListener {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private ArrayList<ElementList> listModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate - start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        listModels = ContentManagerModel.getListChooseModel(getApplicationContext());

        AdapterTemplate adapter = new AdapterTemplate(this, listModels);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.i(TAG, "onCreate - finish");
    }

    @Override
    public void onItemClicked(int position) {
        Log.i(TAG, "onItemClicked - start");
        Model model = new Model(ModelID.values()[listModels.get(position).getID()]);

        DBHelperModel dbHelper = new DBHelperModel(this);
        try {
            int idDBHelper = dbHelper.addModel(model);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.STATE_DETAIL_ACTIVITY, StageDetailActivity.STATE_NEW_FROM_WRITE.toString());
            intent.putExtra(DetailActivity.DATABASE_ID, idDBHelper);
            startActivity(intent);

            setResult(RESULT_OK);
        } catch (NullPointerException e) {
            setResult(RESULT_CANCELED);
        } finally {
            Log.i(TAG, "onItemClicked - finish");
            finish();
        }

    }
}