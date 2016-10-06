package com.modelingbrain.home.detailModel.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;

import com.modelingbrain.home.R;
import com.modelingbrain.home.model.ModelID;

public class StageFragmentEdit extends StageFragment {

    private MultiAutoCompleteTextView multiAutoCompleteTextViews[];

    @Override
    protected void createInterface() {
        Log.i(TAG, "createInterface - start");
        multiAutoCompleteTextViews = new MultiAutoCompleteTextView[model.getModelID().getSize() + 1];
        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createEditElement(model.getName(), 0);
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            createElement(getResources().getStringArray(model.getModelID().getResourceQuestion())[1 + i], QA.QUESTION);
            createEditElement(model.getAnswer(i), i + 1);
        }
        Log.i(TAG, "createInterface - finish");
    }

    @Override
    public void savingModelData() {
        Log.i(TAG, "savingModelData - start");
        if (multiAutoCompleteTextViews == null)
            return;
        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
            if (i == 0) {
                model.setName(multiAutoCompleteTextViews[i].getText().toString());
            } else model.setAnswer(i - 1, multiAutoCompleteTextViews[i].getText().toString());
        }
        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
            Log.i(TAG, "savingModelData - [" + i + "] = "
                    + multiAutoCompleteTextViews[i].getText()
                    + " ID = " + multiAutoCompleteTextViews[i].getId()
            );
        }
        multiAutoCompleteTextViews = null;
        Log.i(TAG, "savingModelData - finish");
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

        if (model.getModelID() == ModelID.ID_PROFILE3) {
            if (position == 1)
                multiAutoCompleteTextViews[position].setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_REP_SYSTEM)));
            if (position == 2)
                multiAutoCompleteTextViews[position].setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_sub_REP_SYSTEM)));
            if (position == 4)
                multiAutoCompleteTextViews[position].setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_dishey)));
            if (position == 5)
                multiAutoCompleteTextViews[position].setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_MetaProgramm)));
            if (position == 6)
                multiAutoCompleteTextViews[position].setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_MetaProgramm_ubeditel)));
            if (position == 8)
                multiAutoCompleteTextViews[position].setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_TricksOfLanguage)));
            if (position == 9)
                multiAutoCompleteTextViews[position].setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_Position)));
            multiAutoCompleteTextViews[position].setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        }

        linLayout.addView(view);
    }
}
