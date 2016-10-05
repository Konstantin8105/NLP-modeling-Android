package com.modelingbrain.home.folderModel;

import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.share.ShareModels;
import com.modelingbrain.home.template.FragmentListWithActionBarTemplate;

import java.util.List;

class ActionModeCallbackFolder implements ActionMode.Callback  {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private final FloatingActionButton fab;
    private final FragmentListWithActionBarTemplate fragment;

    public ActionModeCallbackFolder(FloatingActionButton fab,
                                    FragmentListWithActionBarTemplate fragment) {
        this.fab        = fab;
        this.fragment = fragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.i(TAG, "onCreateActionMode - start");
        mode.getMenuInflater().inflate (R.menu.contex_menu_folder, menu);
        fab.hide();
        Log.i(TAG, "onCreateActionMode - finish");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_remove: {
                Log.i(TAG, "menu_remove");
                DBHelperModel dbHelperModel = new DBHelperModel(fragment.getFragmentContext());
//                ArrayList<Model> items = dbHelperModel.openHeader(ModelState.NORMAL);
                List<Integer> list = fragment.getAdapter().getSelectedItems();
                for (int i = 0; i < list.size(); i++) {
                    dbHelperModel.deleteModel(fragment.getAdapter().get(list.get(i)).getID());
                }
                int count = fragment.getAdapter().getSelectedItems().size();
                if(count > 0){
                    String text = fragment.getFragmentContext().getResources().getQuantityString(R.plurals.remove_models,count,count);
                    Toast.makeText(fragment.getFragmentContext(), text, Toast.LENGTH_SHORT).show();
                }
                fragment.getAdapter().removeItems(fragment.getAdapter().getSelectedItems());
                break;
            }
            case R.id.menu_share: {
                Log.i(TAG, "menu_share");
//                DBHelperModel dbHelperModel = new DBHelperModel(fragment.getFragmentContext());
                List<Integer> list = fragment.getAdapter().getSelectedItems();
                int [] shareModelId = new int [list.size()];
                for(int i=0;i<list.size();i++){
                    shareModelId[i] = fragment.getAdapter().get(list.get(i)).getID();
                }
                ShareModels shareModels = new ShareModels(shareModelId, fragment.getActivity());
                shareModels.showToast();
                shareModels.createEmail();
                break;
            }
            case R.id.menu_to_archive: {
                DBHelperModel dbHelperModel = new DBHelperModel(fragment.getFragmentContext());
                List<Integer> list = fragment.getAdapter().getSelectedItems();
                for(int i=0;i<list.size();i++){
                    dbHelperModel.archiveModel(fragment.getAdapter().get(list.get(i)).getID());
                }
                int count = fragment.getAdapter().getSelectedItems().size();
                if(count > 0){
                    String text = fragment.getFragmentContext().getResources().getQuantityString(R.plurals.archived_models,count,count);
                    Toast.makeText(fragment.getFragmentContext(), text, Toast.LENGTH_SHORT).show();
                }
                fragment.getAdapter().removeItems(fragment.getAdapter().getSelectedItems());
                break;
            }
            default:
                return false;
        }
        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.i(TAG, "onDestroyActionMode - start");
        fragment.nullableActionMode();
        fragment.getAdapter().clearSelection();
        fragment.getAdapter().notifyDataSetChanged();
        fab.show();
        Log.i(TAG, "onDestroyActionMode - finish");
    }
}