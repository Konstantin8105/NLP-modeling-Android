package com.modelingbrain.home.model;

import com.modelingbrain.home.R;

public enum ModelType {
    MODEL,
    NONE,
    DARK,
    STRATEGY;

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
        switch(this)
        {
            case MODEL:
                return 0;
            case STRATEGY:
                return 1;
            case DARK:
                return 2;
            default:
        }
        return 3;
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
        return MODEL.getGeneralColor();
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
        return MODEL.getTextColor();
    }
}