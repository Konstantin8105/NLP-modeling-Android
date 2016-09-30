package com.modelingbrain.home.detailModel;


import android.app.FragmentTransaction;

import com.modelingbrain.home.R;
import com.modelingbrain.home.detailModel.fragments.StageFragment;
import com.modelingbrain.home.detailModel.fragments.StageFragmentEdit;
import com.modelingbrain.home.detailModel.fragments.StageFragmentView;

public enum FragmentType {
    STATE_VIEW_READ( StageFragmentView.class, R.string.detail_mode_read_only, R.drawable.ic_lock),
    STATE_VIEW_WRITE(StageFragmentEdit.class, R.string.detail_mode_write, R.drawable.ic_unlock);

    StageFragment lastFragment = null;
    final Class fragment;
    final int stringResource;
    final int fabIconResource;

    FragmentType(Class fragment, int stringResource, int fabIconResource) {
        this.fragment = fragment;
        this.stringResource = stringResource;
        this.fabIconResource = fabIconResource;
    }

    public StageFragment getNewInstanceFragment(FragmentTransaction transaction) {
        try {
            if(lastFragment != null){
                transaction.remove(lastFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            lastFragment = (StageFragment) fragment.newInstance();
            return lastFragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getStringResource() {
        return stringResource;
    }

    public int getFabIconResource() {
        return fabIconResource;
    }
}
