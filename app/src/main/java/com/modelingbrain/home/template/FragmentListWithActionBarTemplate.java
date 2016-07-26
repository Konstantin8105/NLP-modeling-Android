package com.modelingbrain.home.template;

import android.util.Log;
import android.view.ActionMode;
import android.view.View;

abstract public class FragmentListWithActionBarTemplate extends FragmentListTemplate {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    protected ActionMode actionMode;

    public void nullableActionMode() {
        actionMode = null;
    }

    abstract protected ActionMode.Callback getActionModeCallback();

    private void toggleSelection(int position) {
        Log.d(TAG, "toggleSelection - start");
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        Log.d(TAG, "toggleSelection - count = "+count);
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
        Log.d(TAG, "toggleSelection - finish");
    }

    @Override
    public void onItemClicked(View view, int position) {
        Log.d(TAG, "onItemClicked - start");
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            super.onItemClicked(view, position);
        }
        Log.d(TAG, "onItemClicked - finish");
    }

    @Override
    public boolean onItemLongClicked(View view, int position) {
        Log.d(TAG, "onItemLongClicked - start");
        if (actionMode == null) {
            actionMode = getActivity().startActionMode(getActionModeCallback());
        }
        toggleSelection(position);
        Log.d(TAG, "onItemLongClicked - finish");
        return true;
    }
}
