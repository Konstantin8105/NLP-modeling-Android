package com.modelingbrain.home.detailModel.template;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.R;

public class StageFragment extends Fragment {
    protected Model model;
    protected int generalModelColor;
    protected int generalModelTextColor;

    protected LinearLayout linLayout;

    public void send(Model model) {
        this.model = model;
    }

    protected enum QA{
        QUESTION,
        ANSWER
    }

    protected void initColors() {
        generalModelColor       = this.getResources().getColor(model.getModelType().getGeneralColor());
        generalModelTextColor   = this.getResources().getColor(model.getModelType().getTextColor());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, parentViewGroup, false);

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
