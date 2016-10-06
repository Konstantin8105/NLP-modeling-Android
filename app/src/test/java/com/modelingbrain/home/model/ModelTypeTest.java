package com.modelingbrain.home.model;

import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("ALL")
public class ModelTypeTest {

    @Test
    public void testNotEquals() throws Exception {
        for (int i = 0; i < ModelType.values().length; i++) {
            for (int j = 0; j < ModelType.values().length; j++) {
                if(i!=j){
                    System.out.println("===");
                    System.out.println(ModelType.values()[i].toString());
                    System.out.println(ModelType.values()[j].toString());
                    System.out.println("Same");
                    assertNotEquals(ModelType.values()[i].convert(),ModelType.values()[j].convert());
                    assertNotEquals(ModelType.values()[i].getGeneralColor(),ModelType.values()[j].getGeneralColor());
                    assertNotEquals(ModelType.values()[i].getStringResource(),ModelType.values()[j].getStringResource());
                }
            }
        }
    }
}