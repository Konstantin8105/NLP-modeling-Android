package com.modelingbrain.home.template;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.modelingbrain.home.MainActivity;
import com.modelingbrain.home.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

abstract public class FragmentListTemplate extends Fragment implements
        AdapterSelectableTemplate.ViewHolder.ClickListener {

    protected AdapterSelectableTemplate adapter;

    public AdapterSelectableTemplate getAdapter() {
        return adapter;
    }

    private RecyclerView recyclerView;

    protected Context context;

    public Context getFragmentContext() {
        return context;
    }


    @SuppressWarnings("unused")
    protected final String TAG = this.getClass().toString();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {
        //TODO always update - if list is very big, then it is not comfort
        //TODO don`t save position - if rotate device
        Log.d(TAG, "onCreateView - start");
        View rootView = inflater.inflate(R.layout.fragment_recycle_view, parentViewGroup, false);

        context = getActivity();
        if (context == null)
            throw new RuntimeException("context == null : ");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        updateScreen();

        Log.d(TAG, "onCreateView - finish");
        return rootView;
    }

    abstract protected ArrayList<ElementList> getList();

    //TODO always update - if list is very big, then it is not comfort
    protected void updateScreen() {
        Log.d(TAG, "updateScreen - start");
        ArrayList<ElementList> list = getList();
        Log.d(TAG, "list = " + list.size());
        if (adapter != null)
            adapter.removeAll();
        adapter = new AdapterSelectableTemplate(this, list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "updateScreen - finish");
    }

    abstract protected void intentItemClicked(int position);

    @Override
    public void onItemClicked(View view, int position) {
        Log.d(TAG, "onItemClicked - start");
        intentItemClicked(position);
        Log.d(TAG, "onItemClicked - finish");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult - start");
        if (requestCode == MainActivity.REQUEST_FRAGMENT && resultCode == RESULT_OK) {
            updateScreen();
        }
        Log.d(TAG, "onActivityResult - finish");
    }

    @Override
    public boolean onItemLongClicked(int position) {
        Log.d(TAG, "onItemLongClicked");
        return true;
    }
}
