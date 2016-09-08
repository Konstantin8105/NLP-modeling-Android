package com.modelingbrain.home.folderModel;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ActionMode;

import com.modelingbrain.home.MainActivity;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.detailModel.DetailActivity;
import com.modelingbrain.home.detailModel.StageDetailActivity;
import com.modelingbrain.home.main.ModelSort;
import com.modelingbrain.home.template.ElementList;
import com.modelingbrain.home.template.FragmentListWithActionBarTemplate;

import java.util.ArrayList;

public class FragmentFolder extends FragmentListWithActionBarTemplate {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private FloatingActionButton fab;
    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    private ModelSort modelSort;

    public void changeSort(ModelSort modelSort) {
        this.modelSort = modelSort;
        updateScreen();
    }

    @Override
    protected ArrayList<ElementList> getList() {
        if(modelSort == null)
            modelSort = ModelSort.SortAlphabet;
        return ContentManagerModel.getListNormalModel(getActivity().getApplicationContext(),modelSort);
    }

    @Override
    protected void intentItemClicked(int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.STATE_DETAIL_ACTIVITY, StageDetailActivity.STATE_NORMAL_FROM_READ.toString());
        intent.putExtra(DetailActivity.DATABASE_ID, adapter.get(position).getID());
        startActivityForResult(intent, MainActivity.REQUEST_FRAGMENT);
    }

    @Override
    protected ActionMode.Callback getActionModeCallback() {
        return new ActionModeCallbackFolder(fab,this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "REQUEST_FRAGMENT = " + requestCode);
        Log.d(TAG, "resultCode = " + requestCode);
        if(requestCode == MainActivity.REQUEST_FRAGMENT && resultCode == getActivity().RESULT_OK){
            updateScreen();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
