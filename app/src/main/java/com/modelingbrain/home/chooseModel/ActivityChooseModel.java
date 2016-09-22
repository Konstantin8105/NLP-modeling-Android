package com.modelingbrain.home.chooseModel;

import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.detailModel.DetailActivity;
import com.modelingbrain.home.detailModel.StageDetailActivity;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.R;
import com.modelingbrain.home.model.ModelID;
import com.modelingbrain.home.template.AdapterTemplate;
import com.modelingbrain.home.template.ElementList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;


public class ActivityChooseModel extends AppCompatActivity implements
		AdapterTemplate.ViewHolder.ClickListener{

	@SuppressWarnings("unused")
	private final String TAG = this.getClass().toString();

	private ArrayList<ElementList> listModels;
	private AdapterTemplate adapter;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"onCreate - start");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);

		listModels = ContentManagerModel.getListChooseModel(getApplicationContext());

		adapter = new AdapterTemplate(this,listModels);
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		Log.d(TAG, "onCreate - finish");
	}

	@Override
	public void onItemClicked(int position) {
		Log.d(TAG,"onItemClicked - start");
		Model model = new Model(ModelID.values()[listModels.get(position).getID()]);

		DBHelperModel dbHelper = new DBHelperModel(this);
		int idDBHelper = dbHelper.addModel(model);

		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(DetailActivity.STATE_DETAIL_ACTIVITY, StageDetailActivity.STATE_NEW_FROM_WRITE.toString());
		intent.putExtra(DetailActivity.DATABASE_ID, idDBHelper);
		startActivity(intent);

		setResult(RESULT_OK);
		Log.d(TAG, "onItemClicked - finish");
		finish();
	}
}