package com.modelingbrain.home.detailModel.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.modelingbrain.home.R;
import com.modelingbrain.home.model.Model;

public class StageFragmentView extends StageFragment {

    //TODO: Polar model - special view

    @Override
    protected View initializeData(LayoutInflater inflater, ViewGroup parentViewGroup) {

        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);

        linLayout = (LinearLayout) rootView.findViewById(R.id.fragment_linear_layout);
        linLayout.setBackgroundColor(generalModelColor);

        return rootView;
    }

    @Override
    protected void createInterface() {
        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createElement(model.getName(), QA.ANSWER);
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            createElement(getResources().getStringArray(model.getModelID().getResourceQuestion())[1 + i], QA.QUESTION);
            createElement(model.getAnswer(i), QA.ANSWER);
        }
    }

    @Override
    public Model savingModelData() {
        return model;
    }

}