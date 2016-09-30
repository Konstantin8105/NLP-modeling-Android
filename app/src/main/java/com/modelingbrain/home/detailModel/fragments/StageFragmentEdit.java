package com.modelingbrain.home.detailModel.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;

import com.modelingbrain.home.R;

public class StageFragmentEdit extends StageFragment {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    // TODO: 7/30/16 avoid taking id by hand
    private static final int ID_TEXT_VIEW = 10000;

    private MultiAutoCompleteTextView multiAutoCompleteTextViews[];
    //TODO edit model and saving
    // TODO return changed model

    @Override
    protected View initializeData(LayoutInflater inflater, ViewGroup parentViewGroup) {
        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);

        int[] ids = new int[model.getModelID().getSize() + 1];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = ID_TEXT_VIEW + i;
        }
        multiAutoCompleteTextViews = new MultiAutoCompleteTextView[model.getModelID().getSize() + 1];

//        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
//            multiAutoCompleteTextViews[i] = new MultiAutoCompleteTextView(getActivity().getBaseContext());
//        }

//        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
//            MultiAutoCompleteTextView multiAutoCompleteTextViews = new MultiAutoCompleteTextView(getActivity().getBaseContext());
//            multiAutoCompleteTextViews.setId(ID_TEXT_VIEW + i);
//            Log.d(TAG, "initializeData - ["+i+"] = "+multiAutoCompleteTextViews.getText());
//        }

        linLayout = (LinearLayout) rootView.findViewById(R.id.fragment_linear_layout);
        linLayout.setBackgroundColor(generalModelColor);

        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createEditElement(model.getName(), 0);
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            createElement( getResources().getStringArray(model.getModelID().getResourceQuestion())[1 + i], QA.QUESTION);
            createEditElement(model.getAnswer(i), i + 1);
        }
        return rootView;
    }


    private void createEditElement(String str, int position) {
        // TODO add array for easy edit
        // TODO not correct editable
        // TODO not good view of edit line
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
        multiAutoCompleteTextViews[position].setId(ID_TEXT_VIEW + position);
        linearLayout.setGravity(Gravity.RIGHT);
        //textView.setBackgroundResource(R.drawable.drw_btn_right);
        linLayout.addView(view);
    }

    // TODO: 9/28/16 BIG BUG если перейти в редактирование свернуть и развернуть то глюг

    @Override
    public void savingModelData() {
        Log.d(TAG, "savingModelData - start");
        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
            if (i == 0) {
                model.setName(multiAutoCompleteTextViews[i].getText().toString());
            } else model.setAnswer(i - 1, multiAutoCompleteTextViews[i].getText().toString());
        }
        for (int i = 0; i < multiAutoCompleteTextViews.length; i++) {
            Log.d(TAG, "savingModelData - ["+i+"] = "+multiAutoCompleteTextViews[i].getText());
        }
        multiAutoCompleteTextViews = null;
        Log.d(TAG, "savingModelData - finish");
    }

}
