package com.modelingbrain.home.detailModel.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.modelingbrain.home.R;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class StageFragmentView extends StageFragment {

    @Override
    protected void createInterface() {
        switch (model.getModelID()) {
            case ID_Polar: {
                createPolar();
                break;
            }
            case ID_Rules: {
                createRules();
                break;
            }
            default: {
                createElement(getResources().getString(R.string.model_name), QA.QUESTION);
                createElement(model.getName(), QA.ANSWER);
                for (int i = 0; i < model.getModelID().getSize(); i++) {
                    createElement(getResources().getStringArray(model.getModelID().getResourceQuestion())[1 + i], QA.QUESTION);
                    createElement(model.getAnswer(i), QA.ANSWER);
                }
            }
        }
    }

    @Override
    public void savingModelData() {
    }

    private void createPolar() {
        Log.i(TAG, "createPolar - start");

        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createElement(model.getName(), QA.ANSWER);

        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        View table = ltInflater.inflate(R.layout.view_model_polar, linLayout, false);

        String[] array = getResources().getStringArray(model.getModelID().getResourceQuestion());

        ((TextView) table.findViewById(R.id.textView1)).setText("");

        //Axe -X
        ((TextView) table.findViewById(R.id.textView2)).setText(
                array[2] + ":\n" + model.getAnswer(1));

        //Axe +X
        ((TextView) table.findViewById(R.id.textView3)).setText(
                array[1] + ":\n" + model.getAnswer(0));

        //Axe -Y
        ((TextView) table.findViewById(R.id.textView4)).setText(
                array[4] + ":\n" + model.getAnswer(3));

        //Quadrant (-X, -Y)
        ((TextView) table.findViewById(R.id.textView5)).setText(
                model.getAnswer(7));

        //Quadrant (+X, -Y)
        ((TextView) table.findViewById(R.id.textView6)).setText(
                model.getAnswer(6));

        //Axe +Y
        ((TextView) table.findViewById(R.id.textView7)).setText(
                array[3] + ":\n" + model.getAnswer(2));

        //Quadrant (-X, +Y)
        ((TextView) table.findViewById(R.id.textView8)).setText(
                model.getAnswer(5));

        //Quadrant (+X, +Y)
        ((TextView) table.findViewById(R.id.textView9)).setText(
                model.getAnswer(4));

        linLayout.addView(table);
        Log.i(TAG, "createPolar - finish");
    }

    private void createRules() {
        createElement(getResources().getString(R.string.model_name), QA.QUESTION);
        createElement(model.getName(), QA.ANSWER);

        createElement(getResources().getStringArray(model.getModelID().getResourceQuestion())[1], QA.QUESTION);
        String[] array = model.getAnswer(0).split("\n");
        for (String str : array) {
            if (str.trim().length() > 1)
                createElement(str.trim(), QA.ANSWER);
        }
    }
}