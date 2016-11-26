package com.modelingbrain.home;

import android.app.Fragment;

import com.modelingbrain.home.main.ModelSort;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public interface MainFragment{
    void changeSort(ModelSort sort);
    Fragment getFragment();
}
