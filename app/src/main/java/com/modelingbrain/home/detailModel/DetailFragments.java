package com.modelingbrain.home.detailModel;


import com.modelingbrain.home.R;
import com.modelingbrain.home.detailModel.fragments.StageFragment;
import com.modelingbrain.home.detailModel.fragments.StageFragmentEdit;
import com.modelingbrain.home.detailModel.fragments.StageFragmentView;

public enum DetailFragments {
    STATE_VIEW_READ(new StageFragmentView(), R.string.detail_mode_read_only, R.drawable.ic_lock),
    STATE_VIEW_WRITE(new StageFragmentEdit(), R.string.detail_mode_write, R.drawable.ic_unlock);

    final StageFragment fragment;
    final int stringResource;
    final int fabIconResource;

    DetailFragments(StageFragment fragment, int stringResource, int fabIconResource) {
        this.fragment = fragment;
        this.stringResource = stringResource;
        this.fabIconResource = fabIconResource;
    }

    public StageFragment getFragment() {
        return fragment;
    }

    public int getStringResource() {
        return stringResource;
    }

    public int getFabIconResource() {
        return fabIconResource;
    }
}
