package com.modelingbrain.home.model;

import com.modelingbrain.home.R;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public enum ModelType {
    MODEL(0),
    STRATEGY(1),
    DARK(2),
    NONE(3);

    private final int parameter;

    ModelType(int parameter) {
        this.parameter = parameter;
    }

    public int getStringResource(){
        switch(this)
        {
            case MODEL:
                return R.string.name_model;
            case STRATEGY:
                return R.string.name_strategy;
            case DARK:
                return R.string.name_dark;
            default:
        }
        return R.string.name_other;
    }

    public int convert(){
        return parameter;
    }

    public int getGeneralColor(){
        switch(this)
        {
            case MODEL:
                return R.color.model_color;
            case STRATEGY:
                return R.color.strategy_color;
            case DARK:
                return R.color.dark_color;
            default:
        }
        return R.color.other_color;
    }

    public int getTextColor(){
        switch(this)
        {
            case MODEL:
                return R.color.model_color_text;
            case STRATEGY:
                return R.color.strategy_color_text;
            case DARK:
                return R.color.dark_color_text;
            default:
        }
        return R.color.other_color_text;
    }
}