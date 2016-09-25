package com.modelingbrain.home.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
                    if (parameters.get(i).equals(parameters.get(j))) {
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
                    System.out.println("===");
                    System.out.println(ModelID.arrModelLine[i].modelID.toString());
                    System.out.println(ModelID.arrModelLine[j].modelID.toString());
                    assertNotEquals(ModelID.arrModelLine[i].modelID.getParameter(), ModelID.arrModelLine[j].modelID.getParameter());
                    assertNotEquals(ModelID.arrModelLine[i].resourceIcon, ModelID.arrModelLine[j].resourceIcon);
                    assertNotEquals(ModelID.arrModelLine[i].resourceQuestions, ModelID.arrModelLine[j].resourceQuestions);
                    System.out.println("Same");
                }
            }
        }
    }

    @Test
    public void sameLengthModelId() {
        assertTrue(ModelID.arrModelLine.length == ModelID.values().length);
    }

}