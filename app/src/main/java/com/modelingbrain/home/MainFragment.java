package com.modelingbrain.home;

import android.app.Fragment;

import com.modelingbrain.home.main.ModelSort;

public interface MainFragment{
    void changeSort(ModelSort sort);
    Fragment getFragment();
}
