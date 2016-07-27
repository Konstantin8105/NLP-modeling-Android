package com.modelingbrain.home;

import com.modelingbrain.home.model.ModelID;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestModelId {
    @Test
    public void amountMoreZero() {
        int size = ModelID.values().length;
        assertTrue(size > 0);
    }

    @Test
    public void differentParameter() {
        List<Integer> parameters = new ArrayList<>();
        boolean isAllDiff = true;
        for (int i = 0; i < ModelID.values().length; i++) {
            parameters.add(ModelID.values()[i].getParameter());
        }
        for (int i = 0; i < parameters.size(); i++) {
            for (int j = 0; j < parameters.size(); j++) {
                if (i != j) {
                    if (parameters.get(i) == parameters.get(j)) {
                        isAllDiff = false;
                    }
                }
            }
        }
        assertTrue(isAllDiff);
    }

    @Test
    public void differentModelLine() {
        int size = ModelID.arrModelLine.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    assertNotEquals(ModelID.arrModelLine[i].resourceIcon,ModelID.arrModelLine[j].resourceIcon);
                    assertNotEquals(ModelID.arrModelLine[i].resourceQuestions,ModelID.arrModelLine[j].resourceQuestions);
                }
            }
        }
    }

    @Test
    public void checkConvert() {
        for (int i = 0; i < ModelID.values().length; i++) {
            String name = ModelID.values()[i].toString();
            assertTrue(ModelID.convert(name) == ModelID.valueOf(name));
        }
    }

    @Test
    public void sameLengthModelId() {
        assertTrue(ModelID.arrModelLine.length == ModelID.values().length);
    }

}