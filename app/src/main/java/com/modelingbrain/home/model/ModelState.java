package com.modelingbrain.home.model;

public enum ModelState {
    NORMAL (1), ARCHIVE (500), DELETE (1525);
    private final int value;

    ModelState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ModelState convert(int value) {
        for(int i = 0 ; i < ModelState.values().length; i++){
            if(value == ModelState.values()[i].getValue())
                return ModelState.values()[i];
        }
        return NORMAL;
    }
}