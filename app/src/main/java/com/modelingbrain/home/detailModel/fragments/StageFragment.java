package com.modelingbrain.home.detailModel.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.detailModel.DetailActivity;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.R;

public abstract class StageFragment extends Fragment {

    @SuppressWarnings("unused")
    protected final String TAG = this.getClass().getSimpleName();


    Model model;
    private int generalModelColor;
    int generalModelTextColor;

    LinearLayout linLayout;

    protected enum QA {
        QUESTION,
        ANSWER
    }

    private void initColors() {
        Log.d(TAG, "initColors - start");
        if (model == null) {
            throw new NullPointerException("Model is null");
        }
        generalModelColor = ContextCompat.getColor(
                getActivity().getBaseContext(),
                model.getModelType().getGeneralColor());
        generalModelTextColor = ContextCompat.getColor(
                getActivity().getBaseContext(),
                model.getModelType().getTextColor());
        Log.d(TAG, "initColors - finish");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState - start");
        outState.putInt(DetailActivity.DATABASE_ID, model.getDbId());
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState - finish");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView - start");

        DBHelperModel db = new DBHelperModel(getActivity().getBaseContext());
        if (savedInstanceState != null) {
            model = db.openModel(savedInstanceState.getInt(DetailActivity.DATABASE_ID));
        } else {
            model = db.openModel(getArguments().getInt(DetailActivity.DATABASE_ID));
        }
        db.close();

        Log.d(TAG, "model = " + model.toString());
        initColors();

        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);
        linLayout = (LinearLayout) rootView.findViewById(R.id.fragment_linear_layout);
        linLayout.setBackgroundColor(generalModelColor);

        Log.d(TAG, "onCreateView - finish");
        return rootView;
    }

    protected abstract void createInterface();
    public abstract Model savingModelData();

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart - start");
        linLayout.removeAllViews();
        createInterface();
        Log.d(TAG, "onStart - finish");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume - start");
        super.onResume();
        Log.d(TAG, "onResume - finish");
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    void createElement(String str, QA qa) {
        Log.d(TAG, "createElement - start");
        Log.d(TAG, "createElement - QA : " + qa);
        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        View view = ltInflater.inflate(R.layout.one_row, linLayout, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llOneRow);
        TextView textView = (TextView) view.findViewById(R.id.textOneRow);
        textView.setTextColor(generalModelTextColor);
        textView.setText(str);
        switch (qa) {
            case QUESTION: {
                linearLayout.setGravity(Gravity.LEFT);
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                layoutParams.setMargins(0, 5, 15, 10);
                textView.setBackgroundResource(R.drawable.drw_btn_left);
                break;
            }
            case ANSWER: {
                linearLayout.setGravity(Gravity.RIGHT);
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                layoutParams.setMargins(15, 5, 0, 10);
                linearLayout.setLayoutParams(layoutParams);
                textView.setBackgroundResource(R.drawable.drw_btn_right);
                break;
            }
        }
        linLayout.addView(view);
        Log.d(TAG, "createElement - finish");
    }
}
