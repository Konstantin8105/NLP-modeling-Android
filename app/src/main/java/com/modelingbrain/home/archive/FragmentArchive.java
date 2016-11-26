package com.modelingbrain.home.archive;

import android.app.Fragment;
import android.content.Intent;
import android.view.ActionMode;

import com.modelingbrain.home.MainActivity;
import com.modelingbrain.home.MainFragment;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.detailModel.DetailActivity;
import com.modelingbrain.home.detailModel.StageDetailActivity;
import com.modelingbrain.home.main.ModelSort;
import com.modelingbrain.home.template.ElementList;
import com.modelingbrain.home.template.FragmentListWithActionBarTemplate;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class FragmentArchive extends FragmentListWithActionBarTemplate implements MainFragment {

    private ModelSort modelSort;

    public void changeSort(ModelSort modelSort) {
        this.modelSort = modelSort;
        updateScreen();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    protected ArrayList<ElementList> getList() {
        if(modelSort == null)
            modelSort = ModelSort.SortAlphabet;
        return ContentManagerModel.getListArchiveModel(getActivity().getApplicationContext(),modelSort);
    }

    @Override
    protected void intentItemClicked(int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.STATE_DETAIL_ACTIVITY, StageDetailActivity.STATE_READ_ONLY.toString());
        intent.putExtra(DetailActivity.DATABASE_ID, adapter.get(position).getID());
        startActivityForResult(intent, MainActivity.REQUEST_FRAGMENT);
    }

    @Override
    protected ActionMode.Callback getActionModeCallback() {
        return new ActionModeCallbackArchive(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MainActivity.REQUEST_FRAGMENT && resultCode == RESULT_OK){
            updateScreen();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}