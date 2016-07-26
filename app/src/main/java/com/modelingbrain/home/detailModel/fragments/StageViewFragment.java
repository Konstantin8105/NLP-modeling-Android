package com.modelingbrain.home.detailModel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.modelingbrain.home.R;
import com.modelingbrain.home.detailModel.template.StageFragment;

public class StageViewFragment extends StageFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);

        initColors();

        linLayout = (LinearLayout) rootView.findViewById(R.id.fragment_linear_layout);
        linLayout.setBackgroundColor(generalModelColor);

        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createElement(model.getName(), QA.ANSWER);
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            createElement(getResources().getStringArray(model.getModelID().getResourceQuestion())[1 + i], QA.QUESTION);
            createElement(model.getAnswer(i), QA.ANSWER);
        }
        return rootView;
    }
}