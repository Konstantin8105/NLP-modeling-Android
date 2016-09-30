package com.modelingbrain.home.detailModel;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

import com.modelingbrain.home.R;
import com.modelingbrain.home.detailModel.fragments.StageFragment;
import com.modelingbrain.home.detailModel.fragments.StageFragmentEdit;
import com.modelingbrain.home.detailModel.fragments.StageFragmentView;

import java.util.ArrayList;
import java.util.List;

public class FragmentType {
    public enum Type {
        STATE_VIEW_WRITE,
        STATE_VIEW_READ
    }

    private static class TypeInner {
        final Type type;
        final Class fragment;
        final int stringResource;
        final int fabIconResource;

        public TypeInner(Type type, Class fragment, int stringResource, int fabIconResource) {
            this.type = type;
            this.fragment = fragment;
            this.stringResource = stringResource;
            this.fabIconResource = fabIconResource;
        }
    }

    private static final List<TypeInner> types = new ArrayList<>();

    static {
        types.add(new TypeInner(Type.STATE_VIEW_READ, StageFragmentView.class, R.string.detail_mode_read_only, R.drawable.ic_lock));
        types.add(new TypeInner(Type.STATE_VIEW_WRITE, StageFragmentEdit.class, R.string.detail_mode_write, R.drawable.ic_unlock));
    }

    @SuppressWarnings("unused")
    private final static String TAG = "FragmentType";

    static StageFragment lastFragment = null;

    public static StageFragment getLastFragment() {
        return lastFragment;
    }

    static Type lastType = null;


    public static StageFragment getNewInstanceFragment(Type type, FragmentManager fragmentManager) {
        Log.d(TAG, "getNewInstanceFragment - start");
        if (fragmentManager != null) {
            if (lastFragment != null) {
                Log.d(TAG, "remove last fragment");
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(lastFragment);
                transaction.commit();
            }
        }
        try {
            lastType = type;
            lastFragment = (StageFragment) types.get(positionInArray(type)).fragment.newInstance();
            Log.d(TAG, "getNewInstanceFragment - finish");
            return lastFragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getStringResource() {
        return types.get(positionInArray(lastType)).stringResource;
    }

    public static int getFabIconResource() {
        return types.get(positionInArray(lastType)).fabIconResource;
    }

    private static int positionInArray(Type type) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).type == type)
                return i;
        }
        return -1;
    }
}
