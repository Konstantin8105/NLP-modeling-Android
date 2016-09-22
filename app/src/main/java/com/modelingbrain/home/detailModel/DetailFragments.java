package com.modelingbrain.home.detailModel;


import com.modelingbrain.home.R;
import com.modelingbrain.home.detailModel.fragments.StageEditFragment;
import com.modelingbrain.home.detailModel.fragments.StageViewFragment;
import com.modelingbrain.home.detailModel.template.StageFragment;

public enum DetailFragments {
    // TODO: 22.09.2016 add correct strings
    STATE_VIEW_READ(new StageViewFragment(), R.string.about_program),
    STATE_VIEW_WRITE(new StageEditFragment(), R.string.about_program);

    StageFragment fragment;
    int stringResource;

    DetailFragments(StageFragment fragment, int stringResource) {
        this.fragment = fragment;
        this.stringResource = stringResource;
    }

    public StageFragment getFragment() {
        return fragment;
    }

    public int getStringResource() {
        return stringResource;
    }
}
