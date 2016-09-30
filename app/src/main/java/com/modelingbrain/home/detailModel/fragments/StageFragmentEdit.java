package com.modelingbrain.home.detailModel.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;

import com.modelingbrain.home.R;
import com.modelingbrain.home.model.Model;

public class StageFragmentEdit extends StageFragment {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private MultiAutoCompleteTextView multiAutoCompleteTextViews[];

    @Override
    protected View initializeData(LayoutInflater inflater, ViewGroup parentViewGroup) {
        Log.d(TAG, "initializeData - start");
        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);
        linLayout = (LinearLayout) rootView.findViewById(R.id.fragment_linear_layout);
        linLayout.setBackgroundColor(generalModelColor);
        Log.d(TAG, "initializeData - finish");
        return rootView;
    }

    @Override
    protected void createInterface() {
        Log.d(TAG, "createInterface - start");
        multiAutoCompleteTextViews = new MultiAutoCompleteTextView[model.getModelID().getSize() + 1];
        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createEditElement(model.getName(), 0);
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            createElement(getResources().getStringArray(model.getModelID().getResourceQuestion())[1 + i], QA.QUESTION);
            createEditElement(model.getAnswer(i), i + 1);
        }
        Log.d(TAG, "createInterface - finish");
    }

    @Override
    public Model savingModelData() {
        Log.d(TAG, "savingModelData - start");
        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
            if (i == 0) {
                model.setName(multiAutoCompleteTextViews[i].getText().toString());
            } else model.setAnswer(i - 1, multiAutoCompleteTextViews[i].getText().toString());
        }
        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
            Log.d(TAG, "savingModelData - [" + i + "] = " + multiAutoCompleteTextViews[i].getText());
        }
        multiAutoCompleteTextViews = null;
        Log.d(TAG, "savingModelData - finish");
        return model;
    }

    private void createEditElement(String str, int position) {
        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        View view = ltInflater.inflate(R.layout.one_row_edit, linLayout, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llOneRow);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(15, 5, 0, 10);
        linearLayout.setLayoutParams(layoutParams);
        multiAutoCompleteTextViews[position] = (MultiAutoCompleteTextView) view.findViewById(R.id.editOneRow);
        multiAutoCompleteTextViews[position].setTextColor(generalModelTextColor);
        multiAutoCompleteTextViews[position].setText(str);
        linLayout.addView(view);
    }
}
