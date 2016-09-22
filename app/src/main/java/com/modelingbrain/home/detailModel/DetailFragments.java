package com.modelingbrain.home.detailModel;


import com.modelingbrain.home.R;
import com.modelingbrain.home.detailModel.fragments.StageEditFragment;
import com.modelingbrain.home.detailModel.fragments.StageViewFragment;
import com.modelingbrain.home.detailModel.template.StageFragment;

public enum DetailFragments {
    // TODO: add correct strings
    // TODO: switch icon in fab
    STATE_VIEW_READ(new StageViewFragment(), R.string.about_program, R.drawable.ic_lock),
    STATE_VIEW_WRITE(new StageEditFragment(), R.string.about_program, R.drawable.ic_unlock);

    StageFragment fragment;
    int stringResource;
    int fabIconResource;

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
