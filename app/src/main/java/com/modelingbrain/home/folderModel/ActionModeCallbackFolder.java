package com.modelingbrain.home.folderModel;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

class ActionModeCallbackFolder implements ActionMode.Callback {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private final FloatingActionButton fab;
    private final FragmentListWithActionBarTemplate fragment;

    public ActionModeCallbackFolder(FloatingActionButton fab,
                                    FragmentListWithActionBarTemplate fragment) {
        this.fab = fab;
        this.fragment = fragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.i(TAG, "onCreateActionMode - start");
        mode.getMenuInflater().inflate(R.menu.contex_menu_folder, menu);
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
                final List<Integer> positionsSelectionItems = fragment.getAdapter().getSelectedItems();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.getFragmentContext());
                alertDialogBuilder.setMessage(R.string.dialog_remove);
                alertDialogBuilder.setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DBHelperModel dbHelperModel = new DBHelperModel(fragment.getFragmentContext());
                        for (Integer positionSelectionItem : positionsSelectionItems) {
                            dbHelperModel.deleteModel(fragment.getAdapter().get(positionSelectionItem).getID());
                        }
                        dbHelperModel.close();
                        int count = positionsSelectionItems.size();//fragment.getAdapter().getSelectedItems().size();
                        if (count > 0) {
                            String text = fragment.getFragmentContext().getResources().getQuantityString(R.plurals.remove_models, count, count);
                            Toast.makeText(fragment.getFragmentContext(), text, Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Amount removed items = " + count);
                        fragment.getAdapter().removeItems(positionsSelectionItems);//fragment.getAdapter().getSelectedItems());
                    }
                });

                alertDialogBuilder.setNegativeButton(R.string.answer_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            }
            case R.id.menu_share: {
                Log.i(TAG, "menu_share");
                List<Integer> list = fragment.getAdapter().getSelectedItems();
                int[] shareModelId = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    shareModelId[i] = fragment.getAdapter().get(list.get(i)).getID();
                }
                ShareModels shareModels = new ShareModels(shareModelId, fragment.getActivity());
                shareModels.showToast();
                shareModels.createEmail();
                break;
            }
            case R.id.menu_to_archive: {
                DBHelperModel dbHelperModel = new DBHelperModel(fragment.getFragmentContext());
                List<Integer> positionSelectionItems = fragment.getAdapter().getSelectedItems();
                for (Integer positionSelectionItem : positionSelectionItems) {
                    dbHelperModel.archiveModel(fragment.getAdapter().get(positionSelectionItem).getID());
                }
                dbHelperModel.close();
                int count = fragment.getAdapter().getSelectedItems().size();
                if (count > 0) {
                    String text = fragment.getFragmentContext().getResources().getQuantityString(R.plurals.archived_models, count, count);
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