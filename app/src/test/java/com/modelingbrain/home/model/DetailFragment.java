package com.modelingbrain.home.model;


import com.modelingbrain.home.detailModel.FragmentType;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("ALL")
public class DetailFragment {
    @Test
    public void checkFragment(){
        for (int i = 0; i < FragmentType.Type.values().length; i++) {
            FragmentType.Type type = FragmentType.Type.values()[i];
            Assert.assertNotNull(type.toString(),FragmentType.getNewInstanceFragment(type,null));
        }
    }
}
