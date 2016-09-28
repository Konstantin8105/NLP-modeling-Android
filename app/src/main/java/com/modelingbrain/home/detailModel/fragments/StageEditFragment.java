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
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.detailModel.template.StageFragment;

public class StageEditFragment extends StageFragment {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    // TODO: 7/30/16 avoid taking id by hand
    private static final int ID_TEXT_VIEW = 10000;

    private MultiAutoCompleteTextView multiAutoCompleteTextViews[];
    //TODO edit model and saving
    // TODO return changed model


    // TODO: 28.09.2016 don`t override this method for unificate savedInstance
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);

        if (savedInstanceState != null) {
            model = (new DBHelperModel(getActivity().getBaseContext()))
                    .openModel(savedInstanceState.getInt(modelID));
        }

        initColors();

        int[] ids = new int[model.getModelID().getSize() + 1];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = ID_TEXT_VIEW + i;
        }
        multiAutoCompleteTextViews = new MultiAutoCompleteTextView[model.getModelID().getSize()+1];

        linLayout = (LinearLayout) rootView.findViewById(R.id.fragment_linear_layout);
        linLayout.setBackgroundColor(generalModelColor);

        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createEditElement(model.getName(), 0);
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            createElement(getResources().getStringArray(model.getModelID().getResourceQuestion())[1 + i], QA.QUESTION);
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
        multiAutoCompleteTextViews[position].setId(ID_TEXT_VIEW+position);
        linearLayout.setGravity(Gravity.RIGHT);
        //textView.setBackgroundResource(R.drawable.drw_btn_right);
        linLayout.addView(view);
    }


    @Override
    public void onPause() {
        Log.d(TAG, "onPause() - start");
        for(int i=0;i<multiAutoCompleteTextViews.length;i++){
            Log.d(TAG, "i = " + multiAutoCompleteTextViews[i].getText().toString());
            if(i==0){
                model.setName(multiAutoCompleteTextViews[i].getText().toString());
            }
            else model.setAnswer(i-1,multiAutoCompleteTextViews[i].getText().toString());
        }
        super.onPause();
        Log.d(TAG, "onPause() - finish");
    }
}
