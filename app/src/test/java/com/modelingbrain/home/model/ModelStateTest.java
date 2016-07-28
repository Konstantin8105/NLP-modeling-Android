package com.modelingbrain.home.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelStateTest {

    @Test
    public void testNotEquals() throws Exception {
        for (int i = 0; i < ModelState.values().length; i++) {
            for (int j = 0; j < ModelState.values().length; j++) {
                if(i!=j){
                    System.out.println("===");
                    System.out.println(ModelState.values()[i].toString());
                    System.out.println(ModelState.values()[j].toString());
                    System.out.println("Same");
                    assertNotEquals(ModelState.values()[i].getValue(),ModelState.values()[j].getValue());
                }
            }
        }
    }
}