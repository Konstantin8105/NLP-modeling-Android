package com.modelingbrain.home.template;

import android.util.Log;
import android.view.ActionMode;
import android.view.View;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

abstract public class FragmentListWithActionBarTemplate extends FragmentListTemplate {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().getSimpleName();

    private ActionMode actionMode;

    public void nullableActionMode() {
        actionMode = null;
    }

    abstract protected ActionMode.Callback getActionModeCallback();

    private void toggleSelection(int position) {
        Log.i(TAG, "toggleSelection - start");
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        Log.i(TAG, "toggleSelection - count = "+count);
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
        Log.i(TAG, "toggleSelection - finish");
    }

    @Override
    public void onItemClicked(View view, int position) {
        Log.i(TAG, "onItemClicked - start");
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            super.onItemClicked(view, position);
        }
        Log.i(TAG, "onItemClicked - finish");
    }

    @Override
    public boolean onItemLongClicked(int position) {
        Log.i(TAG, "onItemLongClicked - start");
        if (actionMode == null) {
            actionMode = getActivity().startActionMode(getActionModeCallback());
        }
        toggleSelection(position);
        Log.i(TAG, "onItemLongClicked - finish");
        return true;
    }
}
