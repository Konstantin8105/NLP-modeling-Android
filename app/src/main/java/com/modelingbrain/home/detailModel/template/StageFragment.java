package com.modelingbrain.home.detailModel.template;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.R;

public class StageFragment extends Fragment {
    protected Model model;
    protected final String modelID = "ModelDbId";
    protected int generalModelColor;
    protected int generalModelTextColor;

    protected LinearLayout linLayout;

    public void send(Model model) {
        if (model == null) {
            throw new NullPointerException("Model is null");
        }
        this.model = model;
    }

    protected enum QA {
        QUESTION,
        ANSWER
    }

    protected void initColors() {
        if (model == null) {
            throw new NullPointerException("Model is null");
        }
        generalModelColor = ContextCompat.getColor(
                getActivity().getBaseContext(),
                model.getModelType().getGeneralColor());
        generalModelTextColor = ContextCompat.getColor(
                getActivity().getBaseContext(),
                model.getModelType().getTextColor());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(modelID, model.getDbId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);
        if (savedInstanceState != null) {
            model = (new DBHelperModel(getActivity().getBaseContext()))
                    .openModel(savedInstanceState.getInt(modelID));
        }
        initColors();

        linLayout = (LinearLayout) rootView.findViewById(R.id.fragment_linear_layout);
        linLayout.setBackgroundColor(generalModelColor);

        return rootView;
    }

    protected void createElement(String str, QA qa) {
        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        View view = ltInflater.inflate(R.layout.one_row, linLayout, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llOneRow);
        TextView textView = (TextView) view.findViewById(R.id.textOneRow);
        textView.setTextColor(generalModelTextColor);
        textView.setText(str);
        switch (qa) {
            case QUESTION:
                linearLayout.setGravity(Gravity.LEFT);
                textView.setBackgroundResource(R.drawable.drw_btn_left);
                break;
            case ANSWER:
                linearLayout.setGravity(Gravity.RIGHT);
                textView.setBackgroundResource(R.drawable.drw_btn_right);
                break;
        }
        linLayout.addView(view);
    }
}
