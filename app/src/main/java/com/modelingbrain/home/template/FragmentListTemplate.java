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

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

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
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView - start");
        View rootView = inflater.inflate(R.layout.fragment_recycle_view, parentViewGroup, false);

        context = getActivity();
        if (context == null)
            throw new RuntimeException("context == null : ");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        updateScreen();

        Log.i(TAG, "onCreateView - finish");
        return rootView;
    }

    abstract protected ArrayList<ElementList> getList();

    protected void updateScreen() {
        Log.i(TAG, "updateScreen - start");
        ArrayList<ElementList> list = getList();
        Log.i(TAG, "list = " + list.size());
        if (adapter == null) {
            adapter = new AdapterSelectableTemplate(this, list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swap(list);
        }
        Log.i(TAG, "updateScreen - finish");
    }

    abstract protected void intentItemClicked(int position);

    @Override
    public void onItemClicked(View view, int position) {
        Log.i(TAG, "onItemClicked - start");
        intentItemClicked(position);
        Log.i(TAG, "onItemClicked - finish");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult - start");
        if (requestCode == MainActivity.REQUEST_FRAGMENT && resultCode == RESULT_OK) {
            updateScreen();
        }
        Log.i(TAG, "onActivityResult - finish");
    }

    @Override
    public boolean onItemLongClicked(int position) {
        Log.i(TAG, "onItemLongClicked");
        return true;
    }
}
