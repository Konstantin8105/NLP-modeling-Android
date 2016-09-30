package com.modelingbrain.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

import com.modelingbrain.home.archive.FragmentArchive;
import com.modelingbrain.home.folderModel.FragmentFolder;

import java.util.ArrayList;
import java.util.List;

public class PageStatus {

    public enum PageStatusType {
        Folder,
        Archive
    }

    private static class TypeInner {
        private PageStatusType type;
        private final Class fragment;
        private final int stringResource;

        public TypeInner(PageStatusType type, Class fragment, int stringResource) {
            this.fragment = fragment;
            this.stringResource = stringResource;
            this.type = type;
        }
    }

    private static final List<TypeInner> types = new ArrayList<>();

    static {
        types.add(new TypeInner(PageStatusType.Folder, FragmentFolder.class, R.string.nav_folder));
        types.add(new TypeInner(PageStatusType.Archive, FragmentArchive.class, R.string.nav_archive));
    }

    @SuppressWarnings("unused")
    private final static String TAG = "PageStatus";

    static MainFragment lastFragment = null;

    public static MainFragment getLastFragment() {
        return lastFragment;
    }

    static PageStatusType lastType = null;


    public static MainFragment getNewInstanceFragment(PageStatusType type, FragmentManager fragmentManager) {
        Log.d(TAG, "getNewInstanceFragment - start");
        if (fragmentManager != null) {
            if (lastFragment != null) {
                Log.d(TAG, "remove last fragment");
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(lastFragment.getFragment());
                transaction.commit();
            }
        }
        try {
            lastType = type;
            lastFragment = (MainFragment) types.get(positionInArray(type)).fragment.newInstance();
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

    private static int positionInArray(PageStatusType type) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).type == type)
                return i;
        }
        return -1;
    }
}
