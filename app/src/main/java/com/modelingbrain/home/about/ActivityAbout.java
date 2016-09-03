package com.modelingbrain.home.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.modelingbrain.home.R;

import java.util.ArrayList;

public class ActivityAbout extends AppCompatActivity implements
		AdapterAbout.ViewHolder.ClickListener
{

	private ArrayList<ModelAbout> listModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);

		listModels = ModelAbout.convert(this.getResources().getStringArray(R.array.str_array_About));
		AdapterAbout adapter = new AdapterAbout(this, listModels);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
	}

	@Override
	public void onItemClicked(View view, int position) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(listModels.get(position).link));
		startActivity(i);
	}
}